package com.example.amlr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Esta clase representa la actividad de registro de la aplicación.
 */
public class registro_act extends AppCompatActivity {

    ImageButton back;
    private ScrollView scrollView;
    private LinearLayout linearLayout;
    private int lastScrollY = 0;

    String usuario,cpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Inicialización de elementos de la interfaz
        LinearLayout linearLayout = findViewById(R.id.linear_layout);
        scrollView  = findViewById(R.id.scroll_view);
        back = findViewById(R.id.back);

        for (int i = 1; i <= 16; i++) {
            // Crea un CardView (rectángulo) para representar la información
             final CardView cardView = new CardView(this);
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, // Ancho
                    LinearLayout.LayoutParams.WRAP_CONTENT // Altura (ajustable)
            );
            cardParams.setMargins(16, 16, 16, 16); // Márgenes
            cardView.setLayoutParams(cardParams);

            // Crea un TextView para mostrar información en el CardView
            TextView textView = new TextView(this);
            textView.setText("Información #" + i);
            textView.setTextSize(18);
            textView.setGravity(Gravity.CENTER);
            cardView.addView(textView);

            // Agrega el CardView al LinearLayout
            linearLayout.addView(cardView);

            cardView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    int scrollY = scrollView.getScrollY();
                    // Verifica si el desplazamiento es hacia abajo
                    if (scrollY > lastScrollY) {
                        // Disminuye el tamaño del CardView (ajusta según tus preferencias)
                        int newHeight = cardView.getHeight() - 10; // Cambia 10 por el valor deseado
                        if (newHeight > 0) {
                            ViewGroup.LayoutParams params = cardView.getLayoutParams();
                            params.height = newHeight;
                            cardView.setLayoutParams(params);
                        }
                    }
                    lastScrollY = scrollY;
                }
            });

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(registro_act.this, Menu.class);
                    // Envia los atributos usuario y pass a la activity Menu
                    intent.putExtra("usuario", usuario);
                    intent.putExtra("pass", cpass);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }
}