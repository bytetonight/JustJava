package android.example.com.justjava;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import java.text.NumberFormat;
import java.util.Locale;

import static android.R.attr.name;

public class MainActivity extends AppCompatActivity {

    protected int quantity = 1;
    protected final int price = 5;
    protected final int priceWhippedCream = 1;
    protected final int priceChocolate = 3;
    protected int toppingExtraPrice = 0;

    protected String customerName = "";
    protected boolean hasWhippedCream = false;
    protected boolean hasChocolateTopping = false;
    protected Locale myLocale = Locale.GERMANY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateAndDisplayAll();
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        //quantity = 5;
        //updateAndDisplayAll();
        displayFinalOrderMessage();
    }

    private void calculateExtras()
    {
        toppingExtraPrice = 0;
        if (hasWhippedCream) {

            toppingExtraPrice += priceWhippedCream;
        }

        if (hasChocolateTopping) {

            toppingExtraPrice += priceChocolate;
        }
    }


    private void displayFinalOrderMessage()
    {
        String quantityStr = getResources().getString(R.string.quantity);
        String includingStr = getResources().getString(R.string.including);
        String toppingCremeStr = getResources().getString(R.string.topping_creme);
        String toppingChocStr = getResources().getString(R.string.topping_chocolate);
        String totalStr = getResources().getString(R.string.total);
        String thankYouStr = getResources().getString(R.string.thank_you);
        String requestOrderStr = getResources().getString(R.string.request_order);
        String orderSummaryText = quantityStr + ": " + quantity;

        if (hasWhippedCream) {
            orderSummaryText += "\n" + includingStr + " " + toppingCremeStr;

        }

        if (hasChocolateTopping) {
            orderSummaryText += "\n" + includingStr + " " + toppingChocStr;
        }

        calculateExtras();

        orderSummaryText += "\n" + totalStr + ": ";
        orderSummaryText += NumberFormat.getCurrencyInstance(myLocale).format(quantity * (price + toppingExtraPrice));
        customerName = ((EditText) findViewById(R.id.customer_name)).getText().toString().trim();

        orderSummaryText += String.format("\n" + thankYouStr + " %s!", customerName) ;

        TextView priceTextView = (TextView) findViewById(R.id.price_text_view);
        priceTextView.setText(orderSummaryText);
        //Log.v("orderSummaryText: ", orderSummaryText);

        sendTo(requestOrderStr + " " + customerName, orderSummaryText);

    }

    private boolean sendTo(String subject, String content) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        //intent.putExtra(Intent.EXTRA_EMAIL, email);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        return true;
    }
    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity() {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + quantity);
    }

    /**
     * This method displays the given price on the screen.
     */
    private void displayPrice() {
        calculateExtras();
        TextView priceTextView = (TextView) findViewById(R.id.price_text_view);
        priceTextView.setText(NumberFormat.getCurrencyInstance(myLocale).format(quantity * (price + toppingExtraPrice)));
    }

    public void incrementQuantity(View view)
    {
        ++quantity;

        updateAndDisplayAll();
    }

    public void decrementQuantity(View view)
    {
        --quantity;
        if (quantity < 1)
            quantity = 1;
        updateAndDisplayAll();
    }

    public void includeTopping(View view)
    {
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.whippedCream:
                if (checked)
                    // Put some meat on the sandwich
                    hasWhippedCream = true;
                else
                    hasWhippedCream = false;
                    // Remove the meat
                break;
            case R.id.chocolateTopping:
                if (checked)
                hasChocolateTopping = true;
                else
                hasChocolateTopping = false;
                break;
            // TODO: Veggie sandwich
        }
        calculateExtras();
        displayPrice();
    }

    private void updateAndDisplayAll()
    {
        displayQuantity();
        calculateExtras();
        displayPrice();
    }
}
