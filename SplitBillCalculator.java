import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.geometry.*;

import java.text.NumberFormat;

public class SplitBillCalculator extends Application {

	public double bill_amount = 0;
	public double sales_tax = 0;
	public int split = 0;
	public double tip_amount = 0;
	public double final_amount = 0;
	public double per_person_amount = 0;
	public String result = "";
	public Font heading = new Font("Verdana", 20);
	// Color c = Color.web("0x0000FF");
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
		grid.getColumnConstraints().add(new ColumnConstraints(100));
		grid.getColumnConstraints().add(new ColumnConstraints(150));

		// grid.setGridLinesVisible(true); //for testing purposes

		Scene scene = new Scene(grid, 340, 490);
		primaryStage.setTitle("Split Bill Calculator");

		// ROW 0: HEADING
		Label heading_lbl = new Label("Split Bill Calculator");
		// heading_lbl.setFont(heading);
		heading_lbl.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		heading_lbl.setTextFill(Color.web("14484f"));

		HBox heading_hbox = new HBox(15);
		heading_hbox.getChildren().add(heading_lbl);
		heading_hbox.setAlignment(Pos.TOP_CENTER);
		grid.add(heading_hbox, 0, 0, 2, 1);

		// ROW 1: CHECK AMOUNT
		grid.add(new Label("Check Amount: "), 0, 1);
		TextField bill_tf = new TextField();
		bill_tf.setStyle(beige);
		bill_tf.setText(""); // for testing purposes
		// bill_tf.setOnAction(event -> enterAmount(bill_tf.getText()));
		grid.add(bill_tf, 1, 1);

		// ROW 2: SALES TAX
		grid.add(new Label("Sales Tax: "), 0, 2);
		TextField sales_tax_tf = new TextField();
		sales_tax_tf.setStyle(beige);
		sales_tax_tf.setText(".08875");
		grid.add(sales_tax_tf, 1, 2);

		// ROW 3: TIP COMBOBOX
		grid.add(new Label("Tip Percent: "), 0, 3);
		ComboBox<String> tip_cbo = new ComboBox<>();
		tip_cbo.getItems().addAll("5%", "10%", "15%", "18%", "20%", "25%", "30%");
		tip_cbo.setPrefWidth(150);
		tip_cbo.setStyle(beige);
		tip_cbo.setValue("-Select Tip Amount-");
		grid.add(tip_cbo, 1, 3);

		// ROW 4: SPLIT SPINNER
		grid.add(new Label("Split: "), 0, 4);
		Spinner<Integer> split_spinner = new Spinner<>(1, 10, 2);
		split_spinner.setStyle(beige);
		split_spinner.getValueFactory().setValue(1);
		grid.add(split_spinner, 1, 4);

		// ROW 12,13: OUTPUT - Total | Amount per person | Message
		Label total_lbl = new Label("");
		total_lbl.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
		total_lbl.setWrapText(true);
		Label amt_per_person_lbl = new Label("");
		amt_per_person_lbl.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
		amt_per_person_lbl.setWrapText(true);

		VBox results_vbox = new VBox(10);
		results_vbox.getChildren().add(total_lbl);
		results_vbox.getChildren().add(amt_per_person_lbl);
		grid.add(results_vbox, 0, 12, 2, 1);

		HBox msg_hbox = new HBox(20);
		Label result_lbl = new Label("");
		msg_hbox.getChildren().add(result_lbl);
		result_lbl.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		msg_hbox.setAlignment(Pos.TOP_CENTER);
		grid.add(msg_hbox, 0, 13, 2, 1);

		// ROW 8: 3 BUTTONS
		Button calc_btn = new Button();
		calc_btn.setText("Calculate");
		bill_tf.setOnAction(event -> calculateBill(bill_tf.getText(), sales_tax_tf.getText(), tip_cbo.getValue(),
				split_spinner.getValue(), total_lbl, amt_per_person_lbl, result_lbl));
		calc_btn.setOnAction(event -> calculateBill(bill_tf.getText(), sales_tax_tf.getText(), tip_cbo.getValue(),
				split_spinner.getValue(), total_lbl, amt_per_person_lbl, result_lbl));
		calc_btn.setStyle(darkteal + ";" + white);

		Button clear_btn = new Button();
		clear_btn.setText("Clear");
		clear_btn.setOnAction(event -> clearButtonClicked(bill_tf, sales_tax_tf, tip_cbo, split_spinner, total_lbl,
				amt_per_person_lbl, result_lbl));
		clear_btn.setStyle(darkteal + ";" + white);

		Button exit_btn = new Button();
		exit_btn.setText("Exit");
		exit_btn.setOnAction(event -> exitButtonClicked(primaryStage));
		exit_btn.setStyle(darkteal + ";" + white);

		HBox buttons_hbox = new HBox(15);
		buttons_hbox.getChildren().add(calc_btn);
		buttons_hbox.getChildren().add(clear_btn);
		buttons_hbox.getChildren().add(exit_btn);
		buttons_hbox.setAlignment(Pos.BOTTOM_CENTER);
		grid.add(buttons_hbox, 0, 8, 2, 1);

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void calculateBill(String bill, String tax, String tip, int sp, Label total, Label pp, Label msg) {

		try {
			bill_amount = Double.parseDouble(bill);
			sales_tax = Double.parseDouble(tax);
			String tip_string = tip.substring(0, 2);
			if (tip_string.equals("-S")) {
				tip_amount = 0;
				result = "No tip selected.\n\n";
			} else {
				result = "";
				tip_amount = Integer.parseInt(tip_string);
			}
			split = sp;
			final_amount = bill_amount + (bill_amount * sales_tax)
					+ ((bill_amount + bill_amount * sales_tax) * (tip_amount * .01));
			per_person_amount = (final_amount) / split;

			if (split != 0) {
				NumberFormat nf = NumberFormat.getCurrencyInstance();
				total.setText("TOTAL BILL: " + nf.format(final_amount));
				pp.setText("PER PERSON: " + nf.format(per_person_amount));
				result = result + "Thank you for dining with us today.\nHave a great day!";
				msg.setText(result);
			} else {
				result = "Split amount cannot be zero";
				total.setText("");
				pp.setText("");
				msg.setText(result);
			}

			if ((bill_amount < 0) || (sales_tax < 0)) {
				total.setText("");
				pp.setText("");
				result = "Negative values are not allowed.";
				msg.setText(result);
			}

		} catch (NumberFormatException e) {
			msg.setWrapText(true);
			total.setText("Invalid Entry");
			pp.setText("");
			result = "You have an entered an invalid amount. " + e.getMessage();
			msg.setText(result);
		}

		catch (ArithmeticException e) {
			msg.setWrapText(true);
			total.setText("Invalid Entry");
			pp.setText("");
			result = e.getMessage();
			msg.setText(result);
		}
	}

	public void clearButtonClicked(TextField bill, TextField tax, ComboBox tip, Spinner s, Label total, Label perPerson,
			Label msg) {
		bill.setText("");
		tax.setText("");
		tip.setValue("-Select Tip Amount-");
		s.getValueFactory().setValue(1);
		total.setText("");
		perPerson.setText("");
		msg.setText("");
	}

	public void exitButtonClicked(Stage s) {
		s.close();
	}

	public void enterAmount(String s) {
		double amount = Double.parseDouble(s);
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
