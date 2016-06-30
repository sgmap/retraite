package utils.engine.intern;

import static utils.engine.data.enums.Regime.RegimeType.BASE_AUTRE;

import utils.engine.data.UserChecklistGenerationData;
import utils.engine.data.UserChecklistVars;
import utils.engine.data.enums.Regime;
import utils.engine.data.enums.Regime.RegimeType;

public class UserChecklistVarsProvider {

	public UserChecklistVars provideVars(final UserChecklistGenerationData userChecklistGenerationData) {
		final UserChecklistVars vars = new UserChecklistVars();
		vars.put("regimes_base_hors_alignes", getRegimeBaseHorsAlignes(userChecklistGenerationData));
		vars.put("regimes_compl_hors_agirc_arrco", getRegimeComplHorsAgircArrco(userChecklistGenerationData));
		vars.put("regimes_hors_alignes_et_hors_agirc_arrco", getRegimeHorsAlignesEtHorsAgircArrco(userChecklistGenerationData));
		return vars;
	}

	private String getRegimeBaseHorsAlignes(final UserChecklistGenerationData userChecklistGenerationData) {
		String result = "";
		for (final Regime regime : userChecklistGenerationData.getRegimes()) {
			if (regime.getType() == BASE_AUTRE) {
				if (!result.isEmpty()) {
					result += ",";
				}
				result += regime.getNom();
			}
		}
		return result;
	}

	private String getRegimeComplHorsAgircArrco(final UserChecklistGenerationData userChecklistGenerationData) {
		String result = "";
		for (final Regime regime : userChecklistGenerationData.getRegimes()) {
			if (regime.getType() == RegimeType.COMPLEMENTAIRE && regime != regime.AGIRC_ARRCO) {
				if (!result.isEmpty()) {
					result += ",";
				}
				result += regime.getNom();
			}
		}
		return result;
	}

	private String getRegimeHorsAlignesEtHorsAgircArrco(final UserChecklistGenerationData userChecklistGenerationData) {
		String result = "";
		for (final Regime regime : userChecklistGenerationData.getRegimes()) {
			if (regime.getType() == BASE_AUTRE || (regime.getType() == RegimeType.COMPLEMENTAIRE && regime != regime.AGIRC_ARRCO)) {
				if (!result.isEmpty()) {
					result += ",";
				}
				result += regime.getNom();
			}
		}
		return result;
	}

}
