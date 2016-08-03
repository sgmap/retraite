package utils.dao;

import java.util.List;

import models.Caisse;
import models.CaisseDepartementale;
import utils.engine.data.enums.ChecklistName;

public class CaisseDepartementaleDao {

	public void deleteDepartement(final Long caisseId, final String departement) {
		final Caisse caisse = Caisse.findById(caisseId);
		if (caisse == null) {
			throw new IllegalStateException(
					"Impossible de trouver une caisse pour la caisse d'ID " + caisseId);
		}
		final CaisseDepartementale caisseDepartementale = CaisseDepartementale.find("byCaisseAndDepartement", caisse, departement).first();
		if (caisseDepartementale == null) {
			throw new IllegalStateException(
					"Impossible de trouver une CaisseDepartementale pour la caisse d'ID " + caisseId + " et le département " + departement
							+ ". La caisse concernée est '" + caisse.nom + "'");
		}
		caisseDepartementale.delete();
		deleteCaisseIfNotMoreUsed(caisse);
	}

	public CaisseDepartementale addDepartement(final ChecklistName checklistName, final Long caisseId, final String departement) {
		final Caisse caisse = Caisse.findById(caisseId);
		if (caisse == null) {
			throw new IllegalStateException(
					"Impossible de trouver une caisse pour la caisse d'ID " + caisseId);
		}
		final CaisseDepartementale caisseDepartementale = CaisseDepartementale.find("byCaisseAndDepartement", caisse, departement).first();
		if (caisseDepartementale != null) {
			throw new IllegalStateException(
					"Il existe déjà une CaisseDepartementale pour la caisse d'ID " + caisseId + " et le département " + departement
							+ ". La caisse concernée est '" + caisse.nom + "'");
		}
		final CaisseDepartementale caisseDepartementaleNew = new CaisseDepartementale();
		caisseDepartementaleNew.checklistName = checklistName;
		caisseDepartementaleNew.departement = departement;
		caisseDepartementaleNew.caisse = caisse;
		return caisseDepartementaleNew.save();
	}

	// Privé

	private void deleteCaisseIfNotMoreUsed(final Caisse caisse) {
		final List<CaisseDepartementale> caissesDepartementales = CaisseDepartementale.find("byCaisse", caisse).fetch();
		if (caissesDepartementales.isEmpty()) {
			caisse.delete();
		}
	}

}
