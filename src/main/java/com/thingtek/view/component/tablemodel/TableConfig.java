package com.thingtek.view.component.tablemodel;

import lombok.Data;

import javax.swing.table.TableCellRenderer;
import java.util.Map;

public
@Data
class TableConfig {

    private Map<String, BaseTableModel> tableModels;

    public BaseTableModel getModel(String type) {
        return tableModels.get(type);
    }

    private Map<String, String> decimalreg;

    public String getDecimalReg(String string) {
        return decimalreg.get(string);
    }

    private String datereg;


}
