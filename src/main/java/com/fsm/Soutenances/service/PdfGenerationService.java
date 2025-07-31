package com.fsm.Soutenances.service;

import com.fsm.Soutenances.model.Enseignant;
import com.fsm.Soutenances.model.Soutenance;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class PdfGenerationService {

    public byte[] generateConvocationPdf(Soutenance soutenance) throws Exception {
        // Hada l'buffer fin ghadi nkhzno l'PDF dyalna
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // Charger les polices (fonts)
        PdfFont fontBold = PdfFontFactory.createFont("Helvetica-Bold");
        PdfFont font = PdfFontFactory.createFont("Helvetica");

        // ========== 1. HEADER (Logo et Titre de l'Université) ==========
        // Charger l'image du logo depuis les ressources
        try (InputStream is = getClass().getResourceAsStream("/static/images/logo.jpg")) {
            if (is != null) {
                byte[] imageBytes = is.readAllBytes();
                Image logo = new Image(ImageDataFactory.create(imageBytes));
                logo.scaleToFit(80, 80);
                document.add(logo.setRelativePosition(450, -60, 0, 0));
            }
        }

        Paragraph universite = new Paragraph("Université Moulay Ismail\nFaculté des Sciences - Meknès\nService des Affaires Estudiantines")
                .setFont(font)
                .setFontSize(10)
                .setTextAlignment(TextAlignment.LEFT);
        document.add(universite);

        // ========== 2. TITRE DU DOCUMENT ==========
        Paragraph titre = new Paragraph("CONVOCATION DE SOUTENANCE")
                .setFont(fontBold)
                .setFontSize(18)
                .setMarginTop(30)
                .setMarginBottom(30)
                .setUnderline()
                .setTextAlignment(TextAlignment.CENTER);
        document.add(titre);

        // ========== 3. INFORMATIONS DE LA SOUTENANCE ==========
        // DateTimeFormatter pour formater la date en français
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy 'à' HH:mm", Locale.FRENCH);

        Paragraph intro = new Paragraph("L'étudiant(e) ci-dessous est convoqué(e) pour la soutenance de son projet de fin d'études :")
                .setFont(font).setMarginBottom(20);
        document.add(intro);

        Table infoTable = new Table(UnitValue.createPercentArray(new float[]{1, 2})).setWidth(UnitValue.createPercentValue(100));
        infoTable.addCell(createCell("Étudiant(e):", fontBold));
        infoTable.addCell(createCell(soutenance.getEtudiant().getPrenom() + " " + soutenance.getEtudiant().getNom(), font));
        
        infoTable.addCell(createCell("Sujet:", fontBold));
        infoTable.addCell(createCell(soutenance.getSujet().getTitre(), font));
        
        infoTable.addCell(createCell("Encadrant:", fontBold));
        infoTable.addCell(createCell(soutenance.getSujet().getEncadrant().getPrenom() + " " + soutenance.getSujet().getEncadrant().getNom(), font));
        
        infoTable.addCell(createCell("Date & Heure:", fontBold));
        infoTable.addCell(createCell(soutenance.getDate().format(formatter), font));

        infoTable.addCell(createCell("Salle:", fontBold));
        infoTable.addCell(createCell(soutenance.getSalle().getNom(), font));

        document.add(infoTable);

        // ========== 4. MEMBRES DU JURY ==========
        Paragraph titreJury = new Paragraph("Membres du Jury")
                .setFont(fontBold)
                .setFontSize(14)
                .setMarginTop(20);
        document.add(titreJury);

        Table juryTable = new Table(1).setWidth(UnitValue.createPercentValue(100));
        for (Enseignant jury : soutenance.getJuryEnseignants()) {
            juryTable.addCell(createCell("- " + jury.getPrenom() + " " + jury.getNom() + " (" + jury.getSpecialite() + ")", font));
        }
        document.add(juryTable);

        // ========== 5. FOOTER ==========
        Paragraph footer = new Paragraph("Signature du Service\n\nCachet de l'établissement")
        	    .setFont(font)
        	    .setFontSize(10)
        	    .setFixedPosition(400, 50, 150) // On définit seulement x, y, et la largeur
        	    .setTextAlignment(TextAlignment.RIGHT);;

        document.close();
        return baos.toByteArray();
    }

    // Helper method pour créer des cellules de tableau propres
    private Cell createCell(String text, PdfFont font) {
        return new Cell().add(new Paragraph(text).setFont(font)).setPadding(5).setBorder(null);
    }
}