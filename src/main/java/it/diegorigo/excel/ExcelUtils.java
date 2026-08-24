package it.diegorigo.excel;

import it.diegorigo.excel.annotations.ExcelInfo;
import it.diegorigo.excel.dto.*;
import it.diegorigo.excel.enums.ExcelType;
import it.diegorigo.exceptions.UtilityException;

import java.lang.reflect.Field;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtils {

    public static <T> byte[] generateExcel(List<T> list) throws UtilityException {
        List<Field> fields = getListFieldsOrdered(list.get(0).getClass());
        try (ExcelDocument document = new ExcelDocument(ExcelType.ODS)) {

            ExcelSheet sheet = document.getSheetByIndex(0);

            ExcelRow row = sheet.getRow(0);
            int j = 0;
            for (Field field : fields) {
                String value = field.getName();
                if (field.isAnnotationPresent(ExcelInfo.class)) {
                    ExcelInfo annotation = field.getAnnotation(ExcelInfo.class);
                    field.setAccessible(true);
                    value = annotation.title();
                }
                row.getCell(j++).setStringValue(value);
            }
            for (T element : list) {
                row = sheet.createRow();
                j = 0;
                for (Field field : fields) {
                    ExcelCell cell = row.getCell(j++);
                    writeValue(cell, field.get(element));
                }
            }

            return document.toByteArray();

        } catch (Exception e) {
            throw new UtilityException(e.getMessage());
        }
    }

    public static byte[] generateExcelFromPagesInfo(List<ExcelPageInfo<?>> info) throws UtilityException {
        try (ExcelDocument document = new ExcelDocument(ExcelType.ODS)) {
            for (ExcelPageInfo<?> pageInfo : info) {
                ExcelSheet sheet = document.createSheet(pageInfo.getPageName());
                compilePage(sheet, pageInfo.getContent());
            }

            return document.toByteArray();

        } catch (Exception e) {
            throw new UtilityException(e.getMessage());
        }
    }


    public static <T> void compilePage(ExcelSheet sheet, List<T> list) throws UtilityException {
        try {
            List<Field> fields = getListFieldsOrdered(list.get(0).getClass());
            ExcelRow row = sheet.getRow(0);
            int j = 0;
            for (Field field : fields) {
                String value = field.getName();
                if (field.isAnnotationPresent(ExcelInfo.class)) {
                    ExcelInfo annotation = field.getAnnotation(ExcelInfo.class);
                    field.setAccessible(true);
                    value = annotation.title();
                } else {
                    throw new UtilityException("Annotazione non presente sul campo" + value);
                }
                row.getCell(j++).setStringValue(value);
            }
            for (T element : list) {
                row = sheet.createRow();
                j = 0;
                for (Field field : fields) {
                    ExcelCell cell = row.getCell(j++);
                    writeValue(cell, field.get(element));
                }
            }
        } catch (Exception e) {
            throw new UtilityException(e.getMessage());
        }
    }

    private static <T> void writeValue(ExcelCell cell, T value) {
        if (value == null) {
            cell.setStringValue("");
        } else if (value instanceof LocalTime) {
            cell.setStringValue(((LocalTime) value).format(DateTimeFormatter.ofPattern("HH:mm")));
        } else if (value instanceof Long || value instanceof Integer || value instanceof Double) {
            cell.setNumericValue(Double.parseDouble(value.toString()));
        } else {
            cell.setStringValue(value.toString());
        }
    }

    private static <T> String convertToString(T item) {
        return item.toString();
    }

    public static <T> List<Field> getListFieldsOrdered(Class<T> clazz) {
        Map<Integer, Field> map = new HashMap<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(ExcelInfo.class)) {
                ExcelInfo annotation = field.getAnnotation(ExcelInfo.class);
                field.setAccessible(true);
                map.put(annotation.position(), field);
            }
        }

        return new ArrayList<>(map.keySet()).stream().sorted().map(map::get).toList();
    }
}
