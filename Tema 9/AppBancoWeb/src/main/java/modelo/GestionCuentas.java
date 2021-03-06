package modelo;

import modelo.Cliente;
import modelo.Cuenta;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

public class GestionCuentas {
    private ArrayList<Cuenta> cuentas;

    //Constructor
    public GestionCuentas() throws IOException, ClassNotFoundException {
        Date fecha;
        Properties properties = new Properties();
        properties.load(new FileReader("/Users/carlos/Documents/proyectosjava/APPBancoWeb/AppBancoWeb/src/main/webapp/config/appbancoweb.properties"));
        String ruta = properties.getProperty("fichero");
        File fichero = new File(ruta);
        if (fichero.exists()) {
            FileInputStream fis = new FileInputStream(fichero);
            ObjectInputStream ois = new ObjectInputStream(fis);
            this.cuentas = (ArrayList) ois.readObject();
            ois.close();
        } else {
            System.out.println("Entramos a crear el fichero");
           fichero.createNewFile();
           this.cuentas = new ArrayList<Cuenta>();
            Cliente cliente1 = new Cliente("Carlos", "Barroso Moriana", "77322948F", "1234",
                    "carlosbarrosomoriana@gmail.com", fecha = new Date());
            Cliente cliente2 = new Cliente("Pepe", "Lopez Caro", "12345678Z", "12345",
                    "pepe@pepe", fecha = new Date());
            this.añadirCuenta(cliente1, 1.5f);
            this.añadirCuenta(cliente2, 2.0f);            
            this.SalvarCuentas();
        }
    }
//Getters
    public ArrayList<Cuenta> getCuentas() {
        return cuentas;
    }

    public Cuenta getCuenta(int pos){
        return cuentas.get(pos);
    }
//Otros métodos

    public int contarCuentas(){
        return cuentas.size();
    }

    public int buscarCuenta(Long numero){
        for (Cuenta c: cuentas) {
            if (c.getNumero() == numero){
                return cuentas.indexOf(c);
            }
        }
        return -1;
    }

    public int buscarCuentaMovil(String movil){
        for (Cuenta c: cuentas) {
            if (c.getTitular().getNumMovil().equalsIgnoreCase(movil)){
                return cuentas.indexOf(c);
            }
        }
        return -1;
    }

    public long añadirCuenta(Cliente titular, float interesAnual) {
        Cuenta c;
        long numero = 0;
        boolean numCuenta = false;
        while (!numCuenta){ //Compruebo si el numero ya esta asignado
            numero = (long) (Math.random() * 100000 + 1);
            if (this.buscarCuenta(numero) == -1) numCuenta = true;
        }
        c = new Cuenta(numero, titular, interesAnual);
        cuentas.add(c);
        return numero;
    }

    public Boolean borrarCuenta(Long numero){
        int pos = this.buscarCuenta(numero);
        if (pos == -1) return false;
        else {
            cuentas.remove(pos);
            return true;
        }
    }
    public Cliente buscarCliente(String dni){
        for (Cuenta c: cuentas) {
            if (c.getTitular().getDni().equalsIgnoreCase(dni)) return c.getTitular();
        }
        return null;
    }
    public ArrayList<Cuenta> buscarCuentasCliente(String dni){
        ArrayList<Cuenta> cuentasCliente = new ArrayList<Cuenta>();
        Cuenta cuentaAux;
        for (Cuenta c:this.cuentas) {
            if (c.getTitular().getDni().equalsIgnoreCase(dni)){
                cuentaAux = new Cuenta(c);
                cuentasCliente.add(cuentaAux);
            }
        }
        return cuentasCliente;
    }

    public ArrayList<Cuenta> buscarCuentasenRojos(){
        ArrayList<Cuenta> cuentasEnRojos = new ArrayList<Cuenta>(); //Como mucho un cliente puede tener 10 cuentas
        Cuenta cuentaAux;
        for (Cuenta c:this.cuentas) {
            if (c.enRojos()){
                cuentaAux = new Cuenta(c);
                cuentasEnRojos.add(cuentaAux);
            }
        }
        return cuentasEnRojos;
    }

    public void ingresarInteres(){
        this.cuentas.forEach((c) -> {
            c.ingresoInteresMes();
        });
    }

    public ArrayList<Cliente> buscarClientesTexto(String texto){
        ArrayList<Cliente> clientes = new ArrayList<Cliente>(); //Como mucho un cliente puede tener 10 cuentas
        Cliente clienteAux;
        boolean existe = false;
        for (Cuenta c: cuentas) {
            existe = false;
            if ((c.getTitular().getNombre().toUpperCase().indexOf(texto.toUpperCase()) != -1)
            || (c.getTitular().getApellidos().toUpperCase().indexOf(texto.toUpperCase()) != -1)){ //Compruebo si el texto
                //esta en el nombre o en los apellidos
                for (Cliente temp: clientes){
                    if (temp.getDni().equals(c.getTitular().getDni())) existe = true;
                }
                if (!existe){                    
                    clienteAux = new Cliente(c.getTitular());                    
                    clientes.add(clienteAux);
                }
                
            }
        }
        return clientes;
    }

    public void SalvarCuentas() throws IOException {
        FileOutputStream fos = new FileOutputStream("/Users/carlos/Documents/proyectosjava/APPBancoWeb/AppBancoWeb/src/main/webapp/data/cuentas.dat");
        ObjectOutput oop = new ObjectOutputStream(fos);
        oop.writeObject(this.getCuentas());
        oop.close();
    }

}
