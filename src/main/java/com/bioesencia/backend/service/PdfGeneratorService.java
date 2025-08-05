package com.bioesencia.backend.service;

import com.bioesencia.backend.model.Orden;
import com.bioesencia.backend.model.OrderItem;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class PdfGeneratorService {

    private static final String LOGO_PATH = "/img/LOGO_BIOESENCIA.jpg";
    private static final DecimalFormat FORMAT = new DecimalFormat("₡#,##0.00");

    public byte[] generarPdfOrden(Orden orden) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document documento = new Document(PageSize.A4, 40, 40, 60, 40);
            PdfWriter.getInstance(documento, baos);
            documento.open();

            agregarEncabezado(documento, orden);
            agregarSeccionCliente(documento, orden);
            agregarSeccionProductos(documento, orden);

            documento.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF de orden", e);
        }
    }

    private void agregarEncabezado(Document doc, Orden orden) throws Exception {
        PdfPTable tabla = new PdfPTable(2);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{2f, 3f});

        // Logo (ahora más grande)
        InputStream logoStream = getClass().getResourceAsStream(LOGO_PATH);
        Image logo = Image.getInstance(logoStream.readAllBytes());
        logo.scaleToFit(120, 120);
        PdfPCell logoCell = new PdfPCell(logo);
        logoCell.setBorder(Rectangle.NO_BORDER);
        logoCell.setVerticalAlignment(Element.ALIGN_TOP);
        tabla.addCell(logoCell);

        // Datos de la empresa
        PdfPCell info = new PdfPCell();
        info.setBorder(Rectangle.NO_BORDER);
        info.setHorizontalAlignment(Element.ALIGN_RIGHT);

        Font normal = new Font(Font.HELVETICA, 10);
        Font negrita = new Font(Font.HELVETICA, 10, Font.BOLD);

        Paragraph parrafo = new Paragraph();
        parrafo.add(new Phrase("Cédula: ", negrita));
        parrafo.add(new Phrase("1-1058-0435\n", normal));
        parrafo.add(new Phrase("Teléfono: ", negrita));
        parrafo.add(new Phrase("+506 8362-1394\n", normal));
        parrafo.add(new Phrase("Correo: ", negrita));
        parrafo.add(new Phrase("bioesenciacostarica@gmail.com\n", normal));
        parrafo.add(new Phrase("Código Postal: ", negrita));
        parrafo.add(new Phrase("10601\n", normal));
        parrafo.add(new Phrase("Fecha: ", negrita));
        parrafo.add(new Phrase(orden.getFechaOrden().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), normal));

        info.addElement(parrafo);
        tabla.addCell(info);

        doc.add(tabla);

        Paragraph titulo = new Paragraph("BIOESENCIA", new Font(Font.HELVETICA, 14, Font.BOLD, new Color(94, 167, 67)));
        titulo.setSpacingBefore(10);
        doc.add(titulo);
        doc.add(Chunk.NEWLINE);
    }

    private void agregarSeccionCliente(Document doc, Orden orden) throws Exception {
        Font seccionFont = new Font(Font.HELVETICA, 11, Font.BOLD);
        Font labelFont = new Font(Font.HELVETICA, 10, Font.BOLD);
        Font valueFont = new Font(Font.HELVETICA, 10);

        PdfPCell celdaTitulo = new PdfPCell(new Phrase("Datos del Cliente", seccionFont));
        celdaTitulo.setBackgroundColor(new Color(230, 230, 230));
        celdaTitulo.setColspan(2);
        celdaTitulo.setPadding(6);

        PdfPTable tabla = new PdfPTable(2);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{1, 1});
        tabla.addCell(celdaTitulo);

        tabla.addCell(celdaCampo("Nombre:", orden.getUsuario().getNombre() + " " + orden.getUsuario().getApellido(), labelFont, valueFont));
        tabla.addCell(celdaCampo("Email:", orden.getUsuario().getEmail(), labelFont, valueFont));

        doc.add(tabla);
        doc.add(Chunk.NEWLINE);
    }

    private void agregarSeccionProductos(Document doc, Orden orden) throws Exception {
        Font seccionFont = new Font(Font.HELVETICA, 11, Font.BOLD);
        Font labelFont = new Font(Font.HELVETICA, 10, Font.BOLD);
        Font valueFont = new Font(Font.HELVETICA, 10);

        PdfPTable tabla = new PdfPTable(4);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{3f, 1f, 2f, 2f});

        PdfPCell titulo = new PdfPCell(new Phrase("Productos", seccionFont));
        titulo.setColspan(4);
        titulo.setBackgroundColor(new Color(230, 230, 230));
        titulo.setPadding(6);
        tabla.addCell(titulo);

        tabla.addCell(encabezado("Producto"));
        tabla.addCell(encabezado("Cantidad"));
        tabla.addCell(encabezado("Precio unitario"));
        tabla.addCell(encabezado("Subtotal"));

        double subtotal = 0.0;
        for (OrderItem item : orden.getItems()) {
            double sub = item.getCantidad() * item.getPrecioUnitario().doubleValue();
            subtotal += sub;
            String nombreProducto = item.getProducto() != null ? item.getProducto().getNombre() : "(sin nombre)";
            tabla.addCell(celdaNormal(nombreProducto));
            tabla.addCell(celdaNormal(String.valueOf(item.getCantidad())));
            tabla.addCell(celdaNormal(FORMAT.format(item.getPrecioUnitario())));
            tabla.addCell(celdaNormal(FORMAT.format(sub)));
        }

        double impuesto = subtotal * 0.13;
        double total = subtotal + impuesto;

        tabla.addCell(celdaVacia(2)); tabla.addCell(celdaLabel("Subtotal:")); tabla.addCell(celdaNormal(FORMAT.format(subtotal)));
        tabla.addCell(celdaVacia(2)); tabla.addCell(celdaLabel("Impuestos:")); tabla.addCell(celdaNormal(FORMAT.format(impuesto)));
        tabla.addCell(celdaVacia(2));
        tabla.addCell(new PdfPCell(new Phrase("Total a pagar:", new Font(Font.HELVETICA, 10, Font.BOLD))) {{ setBorder(Rectangle.NO_BORDER); setPadding(4); }});
        tabla.addCell(new PdfPCell(new Phrase(FORMAT.format(total), new Font(Font.HELVETICA, 10, Font.BOLD))) {{ setPadding(4); }});

        doc.add(tabla);
    }

    // Utilidades visuales
    private PdfPCell encabezado(String texto) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, new Font(Font.HELVETICA, 10, Font.BOLD)));
        cell.setBackgroundColor(new Color(245, 245, 245));
        cell.setPadding(6);
        return cell;
    }

    private PdfPCell celdaNormal(String texto) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, new Font(Font.HELVETICA, 10)));
        cell.setPadding(6);
        return cell;
    }

    private PdfPCell celdaLabel(String texto) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, new Font(Font.HELVETICA, 10)));
        cell.setPadding(4);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    private PdfPCell celdaCampo(String label, String value, Font labelFont, Font valueFont) {
        Paragraph p = new Paragraph();
        p.add(new Phrase(label + " ", labelFont));
        p.add(new Phrase(value, valueFont));
        PdfPCell cell = new PdfPCell(p);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(6);
        return cell;
    }

    private PdfPCell celdaVacia(int colspan) {
        PdfPCell cell = new PdfPCell(new Phrase(""));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(colspan);
        return cell;
    }
}
