package com.projet.services;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.projet.models.LigneVente;
import com.projet.models.Vente;

import java.io.FileOutputStream;
import java.util.List;

public class FactureService {

    public static void genererFacture(Vente vente, List<LigneVente> panier) {
        Document document = new Document();

        try {
            // Créer le fichier PDF
            PdfWriter.getInstance(document, new FileOutputStream("Facture_" + vente.getcodeVente() + ".pdf"));
            document.open();
            // Titre de la facture
            Paragraph titre = new Paragraph("FACTURE", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20));
            titre.setAlignment(Element.ALIGN_CENTER);
            document.add(titre);
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Référence Vente: " + vente.getcodeVente()));
            document.add(new Paragraph("Date: " + vente.getDate()));
            document.add(new Paragraph("Type de vente: " + vente.getTypeVente()));
            document.add(new Paragraph(" "));
            // Tableau pour le panier
            PdfPTable table = new PdfPTable(4); // Colonnes: Produit, Prix, Quantité, Total
            table.setWidthPercentage(100);
            // Entêtes
            table.addCell("Produit");
            table.addCell("Prix");
            table.addCell("Quantité");
            table.addCell("Total");
            // Remplir le tableau
            for (LigneVente ligne : panier) {
                table.addCell(ligne.getnomProduit());
                table.addCell(String.valueOf(ligne.getPrix()));
                table.addCell(String.valueOf(ligne.getQuantite()));
                table.addCell(String.valueOf(ligne.getTotal()));
            }
            document.add(table);
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Montant Total: " + vente.getMontant() + " FCFA"));
            document.close();
            System.out.println("Facture générée avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
