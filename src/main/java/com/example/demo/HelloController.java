package com.example.demo;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.converter.DoubleStringConverter;

import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.apache.poi.xwpf.usermodel.*;


public class HelloController {
    private ObservableList<incomeTable> incomeTableObservableList = FXCollections.observableArrayList();
    private ArrayList<userInfo> userInfoArrayList = new ArrayList<>();
    private int coefficient = 2;
    private String incomeReport;
    private String report;

    @FXML
    private Button detailsButton;

    @FXML
    private TextField nameTextField;

    @FXML
    private RadioButton yesRadioButton;

    @FXML
    private RadioButton noRadioButton;

    @FXML
    private DatePicker birthDay1DatePieker;

    @FXML
    private DatePicker birthDay2DatePieker;

    @FXML
    private DatePicker birthDay3DatePieker;

    @FXML
    private Button saveButton;

    @FXML
    private Button clearButton;

    @FXML
    private Button nextButton;

    @FXML
    private Button calculateButton;

    @FXML
    private Button reportButton;

    @FXML
    private ChoiceBox childCountChoiceBox;

    @FXML
    private Label date1Label;

    @FXML
    private Label date2Label;

    @FXML
    private Label date3Label;

    @FXML
    private TableView<incomeTable> incomeTableView;

    @FXML
    private TableColumn<incomeTable, String> monthTableColumn;

    @FXML
    private TableColumn<incomeTable, Double> incomeTableColumn;

    @FXML
    private TableColumn<incomeTable, Double> sumIncomeTableColumn;

    @FXML
    private CheckBox medicalCheckBox;

    @FXML
    private CheckBox educationCheckBox;

    @FXML
    private TextField medicalCostsTextField;

    @FXML
    private TextField educationCostsTextField;

    @FXML
    private TabPane panelTabPane;

    @FXML
    private Tab incomeAndCostTab;

    @FXML
    private void initialize() {
        incomeTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        initIncomeTable();
        childCountChoiceBox.getItems().addAll("0", "1", "2", "3");
        childCountChoiceBox.getSelectionModel().select(0);
        birthDay1DatePieker.setValue(LocalDate.now());
        birthDay2DatePieker.setValue(LocalDate.now());
        birthDay3DatePieker.setValue(LocalDate.now());
        incomeTableView.setEditable(true);
        sumIncomeTableColumn.setEditable(false);
        monthTableColumn.setCellValueFactory(new PropertyValueFactory<incomeTable, String>("month"));
        incomeTableColumn.setCellValueFactory(new PropertyValueFactory<incomeTable, Double>("income"));
        sumIncomeTableColumn.setCellValueFactory(new PropertyValueFactory<incomeTable, Double>("sumIncome"));
        incomeTableColumn.setCellFactory(TextFieldTableCell.<incomeTable, Double>forTableColumn(new DoubleStringConverter() {
            private final NumberFormat nf = DecimalFormat.getNumberInstance();

            {
                nf.setMaximumFractionDigits(2);
                nf.setMinimumFractionDigits(2);
            }

            @Override
            public String toString(final Double value) {
                return nf.format(value);
            }

            @Override
            public Double fromString(final String s) {
                try {
                    return Double.valueOf(s);
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Ошибка при введении данных");
                    alert.setHeaderText("Вы ввели некорректное значение");
                    alert.showAndWait();
                    return 0.00;
                }
            }
        }));
        sumIncomeTableColumn.setCellFactory(TextFieldTableCell.<incomeTable, Double>forTableColumn(new DoubleStringConverter() {
            private final NumberFormat nf = DecimalFormat.getNumberInstance();

            {
                nf.setMaximumFractionDigits(2);
                nf.setMinimumFractionDigits(2);
            }

            @Override
            public String toString(final Double value) {
                return nf.format(value);
            }

            @Override
            public Double fromString(final String s) {
                return Double.valueOf(s);
            }
        }));
        incomeTableColumn.setOnEditCommit((TableColumn.CellEditEvent<incomeTable, Double> t) -> {
            int index = t.getTablePosition().getRow();
            incomeTableObservableList.get(index).setIncome(t.getNewValue());
            incomeTableObservableList.get(index).setSumIncome(getSumIncome(index));
            incomeTableView.refresh();
        });
        incomeTableView.setItems(incomeTableObservableList);
    }

    private void initIncomeTable() {
        incomeTableObservableList.add(new incomeTable("январь"));
        incomeTableObservableList.add(new incomeTable("февраль"));
        incomeTableObservableList.add(new incomeTable("март"));
        incomeTableObservableList.add(new incomeTable("апрель"));
        incomeTableObservableList.add(new incomeTable("май"));
        incomeTableObservableList.add(new incomeTable("июнь"));
        incomeTableObservableList.add(new incomeTable("июль"));
        incomeTableObservableList.add(new incomeTable("август"));
        incomeTableObservableList.add(new incomeTable("сентябрь"));
        incomeTableObservableList.add(new incomeTable("октябрь"));
        incomeTableObservableList.add(new incomeTable("ноябрь"));
        incomeTableObservableList.add(new incomeTable("декабрь"));
        medicalCostsTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*([\\.]\\d{0,2})?")) {
                    medicalCostsTextField.setText(oldValue);
                }
            }
        });
        educationCostsTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*([\\.]\\d{0,2})?")) {
                    educationCostsTextField.setText(oldValue);
                }
            }
        });
        medicalCostsTextField.setText("0.00");
        educationCostsTextField.setText("0.00");
    }

    private double getSumIncome(int month) {
        double sumIncome = 0;
        for (int i = 0; i <= month; i++) {
            sumIncome += incomeTableObservableList.get(i).getIncome();
        }
        return sumIncome;
    }

    @FXML
    protected void childCountChoiceBoxAction() {
        int choice = childCountChoiceBox.getSelectionModel().getSelectedIndex();
        birthDay1DatePieker.setDisable(choice < 1);
        birthDay2DatePieker.setDisable(choice < 2);
        birthDay3DatePieker.setDisable(choice < 3);
        date1Label.setDisable(choice < 1);
        date2Label.setDisable(choice < 2);
        date3Label.setDisable(choice < 3);
    }

    @FXML
    private void yesRadioButtonAction() {
        incomeAndCostTab.setDisable(!yesRadioButton.isSelected());
    }

    @FXML
    private void noRadioButtonAction() {
        incomeAndCostTab.setDisable(noRadioButton.isSelected());
    }

    @FXML
    private void medicalCheckBoxAction() {
        medicalCostsTextField.setDisable(!medicalCheckBox.isSelected());
    }

    @FXML
    private void educationCheckBoxAction() {
        educationCostsTextField.setDisable(!educationCheckBox.isSelected());
    }

    @FXML
    private void saveButtonAction(ActionEvent event) {
        String name = nameTextField.getText();
        boolean incomeInfo;
        if (yesRadioButton.isSelected()) {
            incomeInfo = true;
        } else {
            incomeInfo = false;
        }
        int childCount = childCountChoiceBox.getSelectionModel().getSelectedIndex();
        int childAge1 = Period.between(birthDay1DatePieker.getValue(), LocalDate.now(ZoneId.systemDefault())).getYears();
        int childAge2 = Period.between(birthDay2DatePieker.getValue(), LocalDate.now(ZoneId.systemDefault())).getYears();
        int childAge3 = Period.between(birthDay3DatePieker.getValue(), LocalDate.now(ZoneId.systemDefault())).getYears();
        if (!incomeInfo) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Ошибка в анкете пользователя");
            alert.setHeaderText("Вы не сделали выбор в поле о наличии дохода, облагаемого по ставке 13%, или выбрали вариант НЕТ. " +
                    "\nПри отсутствии дохода вычеты Вам не могут быть предоставлены. " +
                    "\nНажмите Cancel для исправления заполнения анкеты");
            if (alert.showAndWait().get().equals(ButtonType.OK)) {
                clearButtonAction();
                return;
            }
        }
        if (childAge1 >= 18 | childAge2 >= 18 | childAge3 >= 18)
            alertAfter18();
        switch (childCount) {
            case 1: {
                if (childAge1 >= 18) {
                    coefficient = 0;
                } else
                    coefficient--;
            }
            break;
            case 2, 3: {
                if (childAge1 >= 18) {
                    coefficient--;
                }
                if (childAge2 >= 18) {
                    coefficient--;
                }
            }
            break;
            case 0: {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Информация по стандартному вычету");
                alert.setHeaderText("Вы не сделали выбор в поле о количестве детей. " +
                        "\nСтандартный вычет на детей Вам не может быть предоставлен.");
                alert.showAndWait();
                coefficient = 0;
            }
            break;
        }
        userInfo user = new userInfo(name, incomeInfo, childCount, childAge1, childAge2, childAge3);
        userInfoArrayList.add(user);
        nextButton.setDisable(false);
    }

    private Alert alertAfter18() {
        Alert alertAfter18 = new Alert(Alert.AlertType.INFORMATION);
        alertAfter18.setTitle("Информация по стандартному вычету");
        if (nameTextField.getText().isEmpty())
            alertAfter18.setHeaderText("Вычет на детей, достигших 18 лет, не предоставляется!");
        else
            alertAfter18.setHeaderText(nameTextField.getText() + ", вычет на детей, достигших 18 лет, не предоставляется!");
        alertAfter18.showAndWait();
        return alertAfter18;
    }

    @FXML
    private void clearButtonAction() {
        nameTextField.clear();
        yesRadioButton.setSelected(false);
        noRadioButton.setSelected(false);
        childCountChoiceBox.getSelectionModel().select(0);
        birthDay1DatePieker.setValue(LocalDate.now());
        birthDay2DatePieker.setValue(LocalDate.now());
        birthDay3DatePieker.setValue(LocalDate.now());
        nextButton.setDisable(true);
        incomeTableView.getItems().clear();
        initIncomeTable();
        medicalCheckBox.setSelected(false);
        educationCheckBox.setSelected(false);
        medicalCostsTextField.setDisable(true);
        educationCostsTextField.setDisable(true);
        incomeAndCostTab.setDisable(true);
        coefficient = 2;
        reportButton.setDisable(true);
    }

    @FXML
    private void nextButtonAction() {
        panelTabPane.getSelectionModel().select(incomeAndCostTab);
    }

    @FXML
    private void detailsButtonAction() {
        String os = System.getProperty("os.name").toLowerCase();
        String url = "smart.html";
        Runtime rt = Runtime.getRuntime();
        try {
            if (os.contains("win")) {
                rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void calculateButtonAction() {
        double totalIncome = 0;
        for (int i = 0; i < incomeTableView.getItems().size(); i++) {
            totalIncome += Double.parseDouble(String.valueOf(incomeTableView.getColumns().get(1).getCellObservableValue(i).getValue()));
        }
        incomeReport = String.format("%.2f", totalIncome);
        double totalTax = totalIncome * Constants.TAX_RATE;
        double totalDeduction = SocialDeduction() + StandardDeduction();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Информация по вычетам");
        if (totalDeduction <= totalTax) {
            report = "Вы можете вернуть из бюджета сумму уплаченного налога в размере " + String.format("%.2f", totalDeduction) + " руб." +
                    "\nПо стандартному вычету на детей сумма возврата составит " + String.format("%.2f", StandardDeduction()) + " руб." +
                    "\nПо социальному вычету сумма возврата составит " + String.format("%.2f", SocialDeduction()) + " руб.";
            if (nameTextField.getText().isEmpty())
                alert.setHeaderText(report);
            else
                alert.setHeaderText(nameTextField.getText() + ", " + report);
            alert.showAndWait();
        } else {
            report = "Вы можете вернуть из бюджета сумму уплаченного налога в размере " + String.format("%.2f", totalTax) + " руб." +
                    "\nПо стандартному вычету на детей сумма возврата составит " + String.format("%.2f", StandardDeduction()) + " руб." +
                    "\nПо социальному вычету сумма возврата составит " + String.format("%.2f", (totalTax - StandardDeduction())) + " руб.";
            if (nameTextField.getText().isEmpty())
                alert.setHeaderText(report);
            else
                alert.setHeaderText(nameTextField.getText() + ", " + report);
            alert.showAndWait();
        }
        reportButton.setDisable(false);
    }

    private int countMonth() {
        int countMonth = 0;
        for (int i = 0; i < incomeTableView.getItems().size(); i++) {
            if (Double.parseDouble(String.valueOf(incomeTableView.getColumns().get(2).getCellObservableValue(i).getValue())) >= Constants.LIMIT_INCOME)
                break;
            if (Double.parseDouble(String.valueOf(incomeTableView.getColumns().get(2).getCellObservableValue(i).getValue())) == 0)
                countMonth--;
            countMonth++;
        }
        return countMonth;
    }

    private double SocialDeduction() {
        double socialDeduction = 0;
        double amountCosts = Double.parseDouble(medicalCostsTextField.getText()) + Double.parseDouble(educationCostsTextField.getText());
        if (amountCosts <= Constants.LIMIT_COSTS) {
            socialDeduction = amountCosts * Constants.TAX_RATE;
        } else {
            socialDeduction = Constants.LIMIT_COSTS * Constants.TAX_RATE;
        }
        return socialDeduction;
    }

    private double StandardDeduction() {
        double StandardDeduction = 0;
        if (childCountChoiceBox.getSelectionModel().getSelectedIndex() < 3 || Period.between(birthDay3DatePieker.getValue(), LocalDate.now(ZoneId.systemDefault())).getYears() >= 18) {
            StandardDeduction = coefficient * Constants.STANDARD_INDEX1 * Constants.TAX_RATE * countMonth();
        }
        if (childCountChoiceBox.getSelectionModel().getSelectedIndex() == 3 & Period.between(birthDay3DatePieker.getValue(), LocalDate.now(ZoneId.systemDefault())).getYears() < 18) {
            StandardDeduction = (coefficient * Constants.STANDARD_INDEX1 * Constants.TAX_RATE * countMonth()) + (Constants.STANDARD_INDEX2 * Constants.TAX_RATE * countMonth());
        }
        return StandardDeduction;
    }

    @FXML
    private void reportButtonAction() {
        report();
        Runtime rt = Runtime.getRuntime();
        try {
            rt.exec("rundll32 url.dll,FileProtocolHandler " + "Отчет.docx");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void report() {
        XWPFDocument docTemplate;
        try {
            File file = new File("Шаблон.docx");
            FileInputStream fis = new FileInputStream(file);
            docTemplate = new XWPFDocument(fis);
            for (XWPFParagraph p : docTemplate.getParagraphs()) {
                for (XWPFRun r : p.getRuns()) {
                    int pos = r.getTextPosition();
                    String text = r.getText(pos);
                    if (text != null && text.contains("{name}")) {
                        text = text.replace("{name}", nameTextField.getText());
                        r.setText(text, 0);
                        continue;
                    }
                    if (text != null && text.contains("{income}")) {
                        text = text.replace("{income}", incomeReport);
                        r.setText(text, 0);
                        continue;
                    }
                    if (text != null && text.contains("{date}")) {
                        text = text.replace("{date}", LocalDate.now(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                        r.setText(text, 0);
                        continue;
                    }
                    if (text != null && text.contains("{report}")) {
                        text = text.replace("{report}", report);
                        r.setText(text, 0);
                        continue;
                    }
                    if (text != null && text.contains("{childCount}")) {
                        text = text.replace("{childCount}", String.valueOf(childCountChoiceBox.getSelectionModel().getSelectedIndex()));
                        r.setText(text, 0);
                        continue;
                    }
                    if (text != null && text.contains("{medCosts}")) {
                        text = text.replace("{medCosts}", medicalCostsTextField.getText());
                        r.setText(text, 0);
                        continue;
                    }
                    if (text != null && text.contains("{eduCosts}")) {
                        text = text.replace("{eduCosts}", educationCostsTextField.getText());
                        r.setText(text, 0);
                        continue;
                    }
                }
            }
            FileOutputStream outputFile = null;
            try {
                outputFile = new FileOutputStream("Отчет.docx");
                docTemplate.write(outputFile);
            } finally {
                if (outputFile != null) {
                    outputFile.close();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

