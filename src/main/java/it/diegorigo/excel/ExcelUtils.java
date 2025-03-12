package it.diegorigo.excel;

import it.diegorigo.excel.annotations.ExcelInfo;
import it.diegorigo.excel.dto.ExcelDocument;
import it.diegorigo.excel.dto.ExcelRow;
import it.diegorigo.excel.dto.ExcelSheet;
import it.diegorigo.excel.enums.ExcelType;
import it.diegorigo.exceptions.UtilityException;

import java.lang.reflect.Field;
import java.util.*;

public class ExcelUtils {

    public static <T> byte[] generateExcel(List<T> list) throws UtilityException {
        List<Field> fields = getListFieldsOrdered(list.get(0).getClass());
        try (ExcelDocument document = new ExcelDocument(ExcelType.ODS);) {

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
            for (T item : list) {
                row = sheet.createRow();
                j = 0;
                for (Field field : fields) {
                    row.getCell(j++)
                       .setStringValue(Optional.ofNullable(field.get(item))
                                               .map(Object::toString)
                                               .orElse(""));
                }
            }

            return document.toByteArray();

        } catch (Exception e) {
            throw new UtilityException(e.getMessage());
        }
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
