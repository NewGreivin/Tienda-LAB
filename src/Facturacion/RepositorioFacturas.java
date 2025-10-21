/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Facturacion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * @author jprod
 */
public class RepositorioFacturas {
    private static RepositorioFacturas instancia;
    
    private RepositorioFacturas(){
        
    }

    public static RepositorioFacturas getInstancia() {
        if(instancia == null){
            instancia = new RepositorioFacturas();
        }
        return instancia;
    }
    
    
    
    private final Map<Integer, Factura> data = new HashMap<>();
    
    public void guardar(Factura f){
        data.put(f.getNumero(), f);
    }
    
    public Optional<Factura> buscar(int n){
        return Optional.ofNullable(data.get(n));
    }
    
    /**
     * Iterator
     * @return 
     */
    public List<Factura> obtenerTodo(){
        return new ArrayList<>(data.values()); 
    }

    public List<Factura> filtrarPorEstado(EstadoFactura estado){
        return data.values().stream().filter(f->f.getEstado()==estado).toList();
    }
    
    public List<Factura> filtrarPorFecha(LocalDate desde, LocalDate hasta){
        return data.values().stream()
                .filter(f->{
                    var d=f.getFecha().toLocalDate();
                    return (d.isEqual(desde)||d.isAfter(desde)) && (d.isEqual(hasta)||d.isBefore(hasta));
                }).collect(Collectors.toList());
    }
    
}
