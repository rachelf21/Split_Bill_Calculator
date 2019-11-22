import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.geometry.*;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class SplitBillCalculator extends Application {

	public double bill_amount = 0;
	public double sales_tax = 1;
	public int split = 0;
	public double tip_amount = 0;
	public double final_amount = 0;
	public double per_person_amount = 0;
	public String result = "";
	public Font heading = new Font("Verdana", 20);
	Color c = Color.web("0x0000FF");
	String orange = "-fx-background-color: \"0xfcaa47\"";
	String darkteal = "-fx-background-color: \"0x14484f\"";
	String beige = "-fx-background-color: \"0xffd186\"";
	String teal = "-fx-background-color: \"0x02778B\"";
	String lightblue = "-fx-background-color: \"0xcff5fe\"";
	String white = "-fx-text-fill: \"0xffffff\"";

	public void start(Stage primaryStage) {

		GridPane grid = new GridPane();
		grid.setStyle(lightblue);
		grid.setAlignment(Pos.TOP_CENTER);
		grid.setPadding(new Insets(25, 25, 25, 25));
		grid.setHgap(10);
		grid.setVgap(10);

		Scene scene = new Scene(grid, 340, 480);
		primaryStage.setTitle("Split Bill Calculator");

		Label heading_lbl = new Label("Split Bill Calculator");
		// heading_lbl.setFont(heading);
		heading_lbl.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

		HBox headingBox = new HBox(15);
		headingBox.getChildren().add(heading_lbl);
		headingBox.setAlignment(Pos.TOP_CENTER);
		grid.add(headingBox, 0, 0, 2, 1);

		grid.add(new Label("Check Amount: "), 0, 1);
		TextField bill_tf = new TextField();
		bill_tf.setStyle(beige);
		heading_lbl.setTextFill(Color.web("14484f"));
		bill_tf.setOnAction(event -> enterAmount(bill_tf.getText()));
		grid.add(bill_tf, 1, 1);

		grid.add(new Label("Sales Tax: "), 0, 2);
		TextField sales_tax_tf = new TextField();
		sales_tax_tf.setStyle(beige);
		grid.add(sales_tax_tf, 1, 2);

		ComboBox<String> tip_cbo = new ComboBox<>();
		tip_cbo.getItems().addAll("5%", "10%", "15%", "18%", "20%", "25%", "30%");
		tip_cbo.setPrefWidth(150);
		tip_cbo.setStyle(beige);
		tip_cbo.setValue("-Select Tip Amount-");
		grid.add(tip_cbo, 1, 3);

		grid.add(new Label("Tip Percent: "), 0, 3);
		// TextField tip_cbo = new TextField();
		// tip_cbo.setStyle(beige);
		// grid.add(tip_cbo, 1, 3);

		grid.add(new Label("Split: "), 0, 4);
		Spinner<Integer> spinner1 = new Spinner<>(1, 10, 2);
		spinner1.setStyle(beige);
		spinner1.getValueFactory().setValue(1);

		// grid.add(spinner1, 1, 8, 2, 1);
		// TextField split_tf = new TextField();
		// split_tf.setStyle(beige);
		grid.add(spinner1, 1,4);

		Label total_lbl = new Label("");
		// grid.add(total_lbl,0,10);
		total_lbl.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
		total_lbl.setWrapText(true);
		// TextField total_tf = new TextField();
		// total_tf.setStyle(beige);
		// grid.add(total_tf, 1, 10);

		Label amt_per_person_lbl = new Label("");
		// grid.add(amt_per_person_lbl, 0, 12);
		amt_per_person_lbl.setWrapText(true);
		amt_per_person_lbl.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
		amt_per_person_lbl.setWrapText(true);
		// TextField per_person_tf = new TextField();
		// per_person_tf.setStyle(beige);
		// grid.add(per_person_tf, 1, 12);

		HBox resultBox = new HBox(20);
		Label lbl_result = new Label("Enter the amounts where shown.");
		resultBox.getChildren().add(total_lbl);
		// lbl_result.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		grid.add(resultBox, 0, 13, 2, 1);

		HBox resultBox2 = new HBox(20);
		// Label lbl_result = new Label("Enter the amounts where shown.");
		resultBox2.getChildren().add(amt_per_person_lbl);
		grid.add(resultBox2, 0, 16, 2, 1);

		Button btn_calc = new Button();
		btn_calc.setText("Calculate");
		btn_calc.setOnAction(event -> calculateButtonClicked(bill_tf.getText(), sales_tax_tf.getText(),
				tip_cbo.getValue(), spinner1.getValue(), lbl_result, total_lbl, amt_per_person_lbl));
		btn_calc.setStyle(darkteal + ";" + white);
		// btn_calc.setFont(Font.font("Verdana", FontWeight.BOLD, 12));

		Button btn_clear = new Button();
		btn_clear.setText("Clear");
		btn_clear.setOnAction(
				event -> clearButtonClicked(bill_tf, sales_tax_tf, tip_cbo, spinner1, total_lbl, amt_per_person_lbl));
		btn_clear.setStyle(darkteal + ";" + white);

		Button btn_exit = new Button();
		btn_exit.setText("Exit");
		btn_exit.setOnAction(event -> exitButtonClicked(primaryStage));
		btn_exit.setStyle(darkteal + ";" + white);

		HBox buttonBox = new HBox(15);
		buttonBox.getChildren().add(btn_calc);
		buttonBox.getChildren().add(btn_clear);
		buttonBox.getChildren().add(btn_exit);
		buttonBox.setAlignment(Pos.BOTTOM_CENTER);
		grid.add(buttonBox, 0, 8, 2, 1);

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void calculateButtonClicked(String b, String t, String tip, int s, Label l, Label tot, Label pp) {

		try {
			bill_amount = Double.parseDouble(b);
			sales_tax = Double.parseDouble(t);
			String tip_string = tip.substring(0, 2);
			if (tip_string.equals("-S")) {
				tip_amount = 0;
			}

			else {

				tip_amount = Integer.parseInt(tip_string);
				// split = Integer.parseInt(s);
			}
			split = s;
			final_amount = bill_amount + (bill_amount * sales_tax)
					+ ((bill_amount + bill_amount * sales_tax) * (tip_amount * .01));
			per_person_amount = (final_amount) / split;

			if (split != 0) {
				NumberFormat nf = NumberFormat.getCurrencyInstance();
				tot.setText("TOTAL BILL: " + nf.format(final_amount));
				pp.setText("PER PERSON: " + nf.format(per_person_amount));
				result = "Your final amount is: " + nf.format(final_amount);
				// l.setText(result);
			} else {
				result = "Split amount cannot be zero";
				tot.setText(result);
			}

			if ((split < 0) || (sales_tax < 0) || (bill_amount < 0)) {
				tot.setText("Negative values are not allowed.");
			}

		} catch (NumberFormatException e) {
			l.setWrapText(true);
			tot.setText("You have an entered an invalid amount: " + e.getMessage());
			System.out.println("ERROR!\n" + e.getMessage());
		}

		catch (ArithmeticException e) {
			tot.setText("Invalid Entry  " + e.getMessage());
			System.out.println("ERROR!\n" + e.getMessage());
		}

	}

	public void clearButtonClicked(TextField a, TextField b, ComboBox tip, Spinner s, Label total, Label perPerson) {
		a.setText("");
		b.setText("");
		tip.setValue("-Select Tip Amount-");
		s.getValueFactory().setValue(1);
		total.setText("");
		perPerson.setText("");
	}

	public void exitButtonClicked(Stage s) {
		s.close();
		System.out.println("Exiting...");

	}

	public void enterAmount(String s) {
		double amount = Double.parseDouble(s);
		System.out.println("Recording Amount.." + amount);

	}

	class DivideByZeroException extends Exception {

		public String DivideByZeroException(String errorMessage) {
			return "Can't divide by zero";
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}
// Rachel Friedman and Ariel Avshalumov
