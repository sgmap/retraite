package utils.engine;

import static utils.JsonUtils.fromJson;
import static utils.JsonUtils.toJson;
import static utils.engine.EngineUtils.firstNotNull;
import static utils.engine.data.enums.EcranSortie.ECRAN_SORTIE_PENIBILITE;
import static utils.wsinforetraite.InfoRetraiteResult.Status.FOUND;

import java.lang.reflect.Field;

import controllers.data.PostData;
import play.Logger;
import utils.RetraiteException;
import utils.dao.DaoFakeData;
import utils.engine.data.CommonExchangeData;
import utils.engine.data.InfoRetraiteResultRegimeList;
import utils.engine.data.RenderData;
import utils.engine.data.enums.RegimeAligne;
import utils.engine.intern.CalculateurRegimeAlignes;
import utils.engine.utils.AgeCalculator;
import utils.engine.utils.AgeLegalEvaluator;
import utils.wsinforetraite.InfoRetraite;
import utils.wsinforetraite.InfoRetraiteResult;

public class RetraiteEngine {

	private final InfoRetraite infoRetraite;
	private final CalculateurRegimeAlignes calculateurRegimeAlignes;
	private final DaoFakeData daoFakeData;
	private final AgeCalculator ageCalculator;
	private final AgeLegalEvaluator ageLegalEvaluator;
	private final DisplayerLiquidateurQuestions displayerLiquidateurQuestions;
	private final DisplayerDepartureDate displayerDepartureDate;
	private final DisplayerAdditionalQuestions displayerAdditionalQuestions;
	private final DisplayerChecklist displayerChecklist;
	private final DisplayerQuestionCarriereLongue displayerQuestionCarriereLongue;
	private final DisplayerSortieDepartInconnu displayerSortieDepartInconnu;
	private final DisplayerSortiePenibilite displayerSortiePenibilite;
	private final DisplayerSortieTropJeune displayerSortieTropJeune;

	public RetraiteEngine(
			final InfoRetraite infoRetraite,
			final CalculateurRegimeAlignes calculateurRegimeAlignes,
			final DaoFakeData daoFakeData,
			final AgeCalculator ageCalculator,
			final AgeLegalEvaluator ageLegalEvaluator,
			final DisplayerLiquidateurQuestions displayerLiquidateurQuestions,
			final DisplayerDepartureDate displayerDepartureDate,
			final DisplayerAdditionalQuestions displayerAdditionalQuestions,
			final DisplayerChecklist displayerChecklist,
			final DisplayerQuestionCarriereLongue displayerQuestionCarriereLongue,
			final DisplayerSortieDepartInconnu displayerSortieDepartInconnu,
			final DisplayerSortiePenibilite displayerSortiePenibilite,
			final DisplayerSortieTropJeune displayerSortieTropJeune) {

		this.infoRetraite = infoRetraite;
		this.calculateurRegimeAlignes = calculateurRegimeAlignes;
		this.daoFakeData = daoFakeData;
		this.ageCalculator = ageCalculator;
		this.ageLegalEvaluator = ageLegalEvaluator;
		this.displayerLiquidateurQuestions = displayerLiquidateurQuestions;
		this.displayerDepartureDate = displayerDepartureDate;
		this.displayerAdditionalQuestions = displayerAdditionalQuestions;
		this.displayerChecklist = displayerChecklist;
		this.displayerQuestionCarriereLongue = displayerQuestionCarriereLongue;
		this.displayerSortieDepartInconnu = displayerSortieDepartInconnu;
		this.displayerSortiePenibilite = displayerSortiePenibilite;
		this.displayerSortieTropJeune = displayerSortieTropJeune;
	}

	public RenderData processToNextStep(final PostData postData) {
		final RenderData renderData = new RenderData();

		if (postData.hidden_step == null) {
			return displayWelcome(renderData);
		}

		copyHiddenFields(postData, renderData);

		if (postData.hidden_step.equals("welcome")) {
			return displayGetUserData(renderData);
		} else if (postData.hidden_step.equals("getUserData")) {
			Logger.info("Connexion d'un usager : nom=" + postData.nom + " , nir=" + postData.nir + " , naissance=" + postData.naissance);
			if (ageCalculator.getAge(postData.naissance) < 55 && !postData.isForce55) {
				return displaySortieTropJeune(postData, renderData);
			}
			final InfoRetraiteResult regimesInformations = infoRetraite.retrieveAllInformations(postData.nom, postData.nir, postData.naissance);
			if (regimesInformations.status != FOUND) {
				renderData.errorMessage = "Désolé, aucune information n'a pu être trouvée avec les données saisies";
				return displayGetUserData(renderData);
			}
			final String regimes = renderData.hidden_regimes = regimesInformations.extractRegimes();
			renderData.hidden_regimesInfosJsonStr = toJson(regimesInformations.regimes);
			final RegimeAligne[] regimesAlignes = calculateurRegimeAlignes.getRegimesAlignes(regimes);
			if (regimesAlignes.length == 0) {
				return displaySortieAucunRegimeDeBaseAligne(postData, renderData);
			}
			if (regimesAlignes.length >= 2) {
				displayerLiquidateurQuestions.fillData(postData, renderData, regimesAlignes);
				return renderData;
			}
			renderData.hidden_liquidateur = regimesAlignes[0];
			displayerDepartureDate.fillData(postData, renderData, regimes);
		} else if (postData.hidden_step.equals("displayLiquidateurQuestions")) {
			final RegimeAligne[] regimesAlignes = calculateurRegimeAlignes.getRegimesAlignes(postData.hidden_regimes);
			displayerLiquidateurQuestions.fillData(postData, renderData, regimesAlignes);
			if (renderData.ecranSortie == ECRAN_SORTIE_PENIBILITE) {
				displayerSortiePenibilite.fillData(postData, renderData);
				return renderData;
			}
			if (renderData.hidden_liquidateurStep == null) {
				displayerDepartureDate.fillData(postData, renderData, null);
			}
		} else if (postData.hidden_step.equals("displayDepartureDate")) {
			if (postData.departInconnu) {
				displayerSortieDepartInconnu.fillData(postData, renderData);
				renderData.hidden_step = "displaySortieDepartInconnu";
				return renderData;
			}
			if (!ageLegalEvaluator.isAgeLegal(postData.hidden_naissance, postData.departMois, postData.departAnnee)) {
				return displayQuestionCarriereLongue(postData, renderData);
			}
			displayerAdditionalQuestions.fillData(postData, renderData);
		} else if (postData.hidden_step.equals("displayQuestionCarriereLongue")) {
			renderData.hidden_attestationCarriereLongue = true;
			displayerAdditionalQuestions.fillData(postData, renderData);
		} else if (postData.hidden_step.equals("displayAdditionalQuestions") || postData.hidden_step.equals("displayCheckList")) {
			displayerChecklist.fillData(postData, renderData);
		} else {
			throw new RetraiteException("Situation anormale : l'étape '" + postData.hidden_step + "' pour le traitement");
		}
		return renderData;
	}

	private RenderData displaySortieTropJeune(final PostData postData, final RenderData renderData) {
		displayerSortieTropJeune.fillData(postData, renderData);
		return renderData;
	}

	private RenderData displaySortieAucunRegimeDeBaseAligne(final PostData postData, final RenderData renderData) {
		renderData.hidden_step = "displaySortieAucunRegimeDeBaseAligne";
		renderData.regimesInfosAucunRegimeDeBaseAligne = fromJson(postData.hidden_regimesInfosJsonStr, InfoRetraiteResultRegimeList.class);
		return renderData;
	}

	private RenderData displayQuestionCarriereLongue(final PostData data, final RenderData renderData) {
		displayerQuestionCarriereLongue.fillData(data, renderData);
		return renderData;
	}

	private RenderData displayWelcome(final RenderData renderData) {
		renderData.hidden_step = "welcome";
		return renderData;
	}

	private RenderData displayGetUserData(final RenderData renderData) {
		renderData.hidden_step = "getUserData";

		// Temporaire pour afficher les DataRegime tant qu'on ne peut pas interroger le WS info-retraite
		renderData.fakeData = daoFakeData.findAll();
		return renderData;
	}

	private void copyHiddenFields(final PostData data, final RenderData renderData) {
		try {
			for (final Field field : CommonExchangeData.class.getFields()) {
				final String fieldName = field.getName();
				if (fieldName.startsWith("hidden_") && !fieldName.toLowerCase().contains("step")) {
					final Object hiddenData = field.get(data);
					final Object noHiddenData = tryToGetNoHiddenData(data, fieldName);
					field.set(renderData, firstNotNull(noHiddenData, hiddenData));
				}
			}
		} catch (final Exception e) {
			throw new RetraiteException("Error copying hidden fields", e);
		}
	}

	private Object tryToGetNoHiddenData(final PostData data, final String fieldName) throws IllegalAccessException {
		final String noHiddenFieldName = fieldName.substring("hidden_".length());
		try {
			final Field fieldInPostData = PostData.class.getField(noHiddenFieldName);
			return fieldInPostData.get(data);
		} catch (final NoSuchFieldException e) {
			return null;
		}
	}

}
