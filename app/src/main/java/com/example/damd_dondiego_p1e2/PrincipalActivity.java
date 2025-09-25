package com.example.damd_dondiego_p1e2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class PrincipalActivity extends AppCompatActivity {

    ArrayList<String> datos = null;
    EditText txtnombre, txtcorreo, txtedad;
    GridView gvdatos;
    ArrayAdapter<String> adaptador;
    private int posicionSeleccionada = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_principal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtnombre = findViewById(R.id.txtnombre);
        txtcorreo = findViewById(R.id.txtcorreo);
        txtedad = findViewById(R.id.txtedad);
        gvdatos = findViewById(R.id.gvdatos);

        habilitarCampos(false);

        inicializarDatos();
        configurarGridView();
    }

    private void habilitarCampos(boolean habilitar) {
        txtnombre.setEnabled(habilitar);
        txtcorreo.setEnabled(habilitar);
        txtedad.setEnabled(habilitar);
    }

    private void limpiarCampos() {
        txtnombre.setText("");
        txtcorreo.setText("");
        txtedad.setText("");
    }

    private void inicializarDatos() {
        if (datos == null) {
            datos = new ArrayList<>();
            cargarInfo();
        }
        adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datos);
        gvdatos.setAdapter(adaptador);
    }

    private void configurarGridView() {
        gvdatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 3 && position % 3 == 0) {
                    posicionSeleccionada = position;

                    String nombre = datos.get(position);
                    String edad = datos.get(position + 1);
                    String correo = datos.get(position + 2);

                    txtnombre.setText(nombre);
                    txtedad.setText(edad);
                    txtcorreo.setText(correo);

                    habilitarCampos(false);

                    Toast.makeText(PrincipalActivity.this,
                            "Registro seleccionado: " + nombre, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void btnNuevo(View v) {
        posicionSeleccionada = -1;
        limpiarCampos();
        habilitarCampos(true);
        Toast.makeText(this, "Ingrese los datos del nuevo registro", Toast.LENGTH_SHORT).show();
    }

    public void btnEditar(View v) {
        if (posicionSeleccionada == -1) {
            Toast.makeText(this, "Seleccione un registro para editar", Toast.LENGTH_SHORT).show();
            return;
        }
        habilitarCampos(true);
        Toast.makeText(this, "Puede editar la información", Toast.LENGTH_SHORT).show();
    }

    public void btnGrabar(View v) {
        String nombre = txtnombre.getText().toString().trim();
        String edad = txtedad.getText().toString().trim();
        String correo = txtcorreo.getText().toString().trim();

        if (nombre.isEmpty() || edad.isEmpty() || correo.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        if (posicionSeleccionada == -1) {
            datos.add(nombre);
            datos.add(edad);
            datos.add(correo);
            Toast.makeText(this, "Nuevo registro guardado", Toast.LENGTH_SHORT).show();
        } else {
            datos.set(posicionSeleccionada, nombre);
            datos.set(posicionSeleccionada + 1, edad);
            datos.set(posicionSeleccionada + 2, correo);
            Toast.makeText(this, "Registro actualizado correctamente", Toast.LENGTH_SHORT).show();
        }

        ArchivoTXT info = new ArchivoTXT();
        if (info.EscribirArchivo(this, datos)) {
            adaptador.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "Error al guardar la información", Toast.LENGTH_LONG).show();
        }

        limpiarCampos();
        habilitarCampos(false);
        posicionSeleccionada = -1;
    }

    public void btnEliminar(View v) {
        if (posicionSeleccionada == -1) {
            Toast.makeText(this, "Seleccione un registro para eliminar", Toast.LENGTH_SHORT).show();
            return;
        }

        datos.remove(posicionSeleccionada + 2);
        datos.remove(posicionSeleccionada + 1);
        datos.remove(posicionSeleccionada);

        ArchivoTXT info = new ArchivoTXT();
        if (info.EscribirArchivo(this, datos)) {
            adaptador.notifyDataSetChanged();
            posicionSeleccionada = -1;
            limpiarCampos();
            Toast.makeText(this, "Registro eliminado correctamente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al eliminar el registro", Toast.LENGTH_SHORT).show();
        }
    }

    public void cargarInfo() {
        ArchivoTXT info = new ArchivoTXT();
        if (info.LeerArchivo(this) && info.getDatos().size() > 0) {
            datos = info.getDatos();
        } else {
            datos = new ArrayList<>();
            datos.add("Nombre");
            datos.add("Edad");
            datos.add("Correo");
        }
    }
}