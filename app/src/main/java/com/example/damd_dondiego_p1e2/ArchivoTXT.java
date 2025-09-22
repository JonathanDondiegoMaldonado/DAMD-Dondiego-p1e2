package com.example.damd_dondiego_p1e2;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class ArchivoTXT {

    private final String NomArch = "datos.txt";
    ArrayList<String> datos = new ArrayList<>();

    public boolean LeerArchivo(Context ctx){
        try{
            InputStreamReader archivo = new InputStreamReader(ctx.openFileInput(NomArch));
            BufferedReader br = new BufferedReader(archivo);
            String linea;

            datos.add("Nombre");
            datos.add("Edad");
            datos.add("Correo");

            while((linea = br.readLine()) != null){
                datos.add(linea);
            }
            br.close();
            archivo.close();
        }catch (Exception e){
            datos.clear();
            datos.add("Nombre");
            datos.add("Edad");
            datos.add("Correo");
            return false;
        }
        return true;
    }

    public ArrayList<String> getDatos(){
        return datos;
    }

    public boolean EscribirArchivo(Context ctx, ArrayList<String> Datos){
        try{
            OutputStreamWriter archivo = new OutputStreamWriter(ctx.openFileOutput(NomArch, Context.MODE_PRIVATE));

            for(int i = 3; i < Datos.size(); i++){
                archivo.write(Datos.get(i) + "\n");
            }
            archivo.flush();
            archivo.close();
        }catch (Exception e){
            return false;
        }
        return true;
    }
}