package com.ikalogic.ika.adapters;

public class Persona {
    private final String nombre;
    private final String telefono;
    public Persona(String nombre,String telefono) {
        this.nombre=nombre;
        this.telefono=telefono;
    }

    public String getNombre()
    {
        return nombre;
    }

    public String getTelefono()
    {
        return telefono;
    }
}
