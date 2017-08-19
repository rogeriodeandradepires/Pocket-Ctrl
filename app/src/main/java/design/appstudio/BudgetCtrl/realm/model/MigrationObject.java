package design.appstudio.BudgetCtrl.realm.model;

import android.support.annotation.NonNull;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

//@IgnoreExtraProperties
public class MigrationObject extends RealmObject implements Comparable<MigrationObject> {


    @PrimaryKey
    private long id;

    private Date dateRegOrUpd;
    private String descricao;
    private String categoria;
    private double valor;
    private String dataVenc;
    private String tipo;

    private int icon;
    private boolean isGrouped;

    public MigrationObject() {
        // Default constructor required for calls to DataSnapshot.getValor(User.class)
    }


    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDataVenc() {
        return dataVenc;
    }

    public void setDataVenc(String dataVenc) {
        this.dataVenc = dataVenc;
    }

    public Date getDateRegOrUpd() {
        return dateRegOrUpd;
    }

    public void setDateRegOrUpd(Date dateRegOrUpd) {
        this.dateRegOrUpd = dateRegOrUpd;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean getIsGrouped() {
        return isGrouped;
    }

    public void setIsGrouped(boolean isGrouped) {
        isGrouped = isGrouped;
    }

    @Override
    public int compareTo(@NonNull MigrationObject o) {
        return getDateRegOrUpd().compareTo(o.getDateRegOrUpd());
    }

    public Despesa toDespesa(MigrationObject migrationObject){
        Despesa despesa = new Despesa();
        despesa.setId(getId());
        despesa.setCategoria(getCategoria());
        despesa.setDescricao(getDescricao());
        despesa.setDataVenc(getDataVenc());
        despesa.setValor(getValor());
        despesa.setIcon(getIcon());
        despesa.setDateRegOrUpd(getDateRegOrUpd());
        despesa.setTipo("Despesa");

        return despesa;
    }

    public Receita toReceita(MigrationObject migrationObject){
        Receita receita = new Receita();
        receita.setId(getId());
        receita.setCategoria(getCategoria());
        receita.setDescricao(getDescricao());
        receita.setDataVenc(getDataVenc());
        receita.setValor(getValor());
        receita.setIcon(getIcon());
        receita.setDateRegOrUpd(getDateRegOrUpd());
        receita.setTipo("Receita");
        return receita;
    }

}


