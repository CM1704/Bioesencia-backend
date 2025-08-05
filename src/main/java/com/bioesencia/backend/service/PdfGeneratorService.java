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
            agregarCliente(documento, orden);
            agregarTablaProductos(documento, orden);

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

        // Logo
        InputStream logoStream = getClass().getResourceAsStream(LOGO_PATH);
        Image logo = Image.getInstance(logoStream.readAllBytes());
        logo.scaleToFit(100, 100);
        PdfPCell logoCell = new PdfPCell(logo);
        logoCell.setBorder(Rectangle.NO_BORDER);
        logoCell.setRowspan(2);
        tabla.addCell(logoCell);

        // Nombre empresa (debajo del logo)
        PdfPCell nombreEmpresa = new PdfPCell(new Phrase("BIOESENCIA", new Font(Font.HELVETICA, 18, Font.BOLD, new Color(93, 93, 93))));
        nombreEmpresa.setBorder(Rectangle.NO_BORDER);
        nombreEmpresa.setHorizontalAlignment(Element.ALIGN_LEFT);
        tabla.addCell(nombreEmpresa);

        // Info empresa
        PdfPTable info = new PdfPTable(1);
        info.setWidthPercentage(100);
        info.addCell(celdaTexto("Cédula Jurídica: 1-1058-0435"));
        info.addCell(celdaTexto("Tel: +506 8362-1394"));
        info.addCell(celdaTexto("Correo: bioesenciacostarica@gmail.com"));
        info.addCell(celdaTexto("Código Postal: 10601"));
        info.addCell(celdaTexto("Orden #: " + orden.getId()));
        info.addCell(celdaTexto("Fecha: " + orden.getFechaOrden().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));

        PdfPCell infoCell = new PdfPCell(info);
        infoCell.setBorder(Rectangle.NO_BORDER);
        tabla.addCell(infoCell);

        doc.add(tabla);
        doc.add(Chunk.NEWLINE);
    }

    private void agregarCliente(Document doc, Orden orden) throws Exception {
        PdfPTable tabla = new PdfPTable(1);
        tabla.setWidthPercentage(100);
        tabla.addCell(crearCeldaTitulo("Cliente"));
        tabla.addCell(crearCeldaValor("Nombre: " + orden.getUsuario().getNombre() + " " + orden.getUsuario().getApellido()));
        tabla.addCell(crearCeldaValor("Correo: " + orden.getUsuario().getEmail()));
        doc.add(tabla);
        doc.add(Chunk.NEWLINE);
    }

    private void agregarTablaProductos(Document doc, Orden orden) throws Exception {
        PdfPTable tabla = new PdfPTable(4);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{4, 1.5f, 2, 2});

        tabla.addCell(celdaEncabezado("Producto"));
        tabla.addCell(celdaEncabezado("Cantidad"));
        tabla.addCell(celdaEncabezado("Precio Unitario"));
        tabla.addCell(celdaEncabezado("Subtotal"));

        double subtotal = 0.0;

        for (OrderItem item : orden.getItems()) {
            double sub = item.getCantidad() * item.getPrecioUnitario().doubleValue();
            subtotal += sub;

            tabla.addCell(celdaTexto(item.getProducto().getNombre()));
            tabla.addCell(celdaTexto(String.valueOf(item.getCantidad())));
            tabla.addCell(celdaTexto(FORMAT.format(item.getPrecioUnitario())));
            tabla.addCell(celdaTexto(FORMAT.format(sub)));
        }

        double impuestos = subtotal * 0.13;
        double total = subtotal + impuestos;

        PdfPCell vacio = new PdfPCell(new Phrase(""));
        vacio.setColspan(2);
        vacio.setBorder(Rectangle.NO_BORDER);

        tabla.addCell(vacio);
        tabla.addCell(celdaTexto("Subtotal:"));
        tabla.addCell(celdaTexto(FORMAT.format(subtotal)));

        tabla.addCell(vacio);
        tabla.addCell(celdaTexto("Impuestos:"));
        tabla.addCell(celdaTexto(FORMAT.format(impuestos)));

        tabla.addCell(vacio);
        PdfPCell totalCell = new PdfPCell(new Phrase("Total a pagar:", new Font(Font.HELVETICA, 12, Font.BOLD)));
        totalCell.setBorder(Rectangle.NO_BORDER);
        tabla.addCell(totalCell);
        tabla.addCell(celdaTexto(FORMAT.format(total)));

        doc.add(tabla);
    }

    private PdfPCell celdaEncabezado(String texto) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, new Font(Font.HELVETICA, 12, Font.BOLD, Color.WHITE)));
        cell.setBackgroundColor(new Color(94, 167, 67));
        cell.setPadding(6);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    private PdfPCell celdaTexto(String texto) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, new Font(Font.HELVETICA, 11)));
        cell.setPadding(6);
        return cell;
    }

    private PdfPCell crearCeldaTitulo(String texto) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, new Font(Font.HELVETICA, 14, Font.BOLD)));
        cell.setPaddingBottom(8);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    private PdfPCell crearCeldaValor(String texto) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, new Font(Font.HELVETICA, 12)));
        cell.setPaddingBottom(4);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }
}
