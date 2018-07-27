package com.proyecto.michaelmatamoros.averias.bd;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


    @DatabaseTable(tableName = "usuarios")
    public class Usuario {

        @DatabaseField(generatedId = true, columnName = "user_id", canBeNull = false)
        public int userId;

        @DatabaseField(columnName = "username", canBeNull = false)
        public String username;

        @DatabaseField(columnName = "password", canBeNull = false)
        public String password;

        @DatabaseField(columnName = "name", canBeNull = false)
        public String nombre;

        @DatabaseField(columnName = "correo", canBeNull = false)
        public String correo;

        @DatabaseField(columnName = "telefono", canBeNull = false)
        public String telefono;

        @DatabaseField(columnName = "cedula", canBeNull = false)
        public String cedula;

        public Usuario() {}
}
