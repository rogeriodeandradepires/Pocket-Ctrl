package design.appstudio.BudgetCtrl.realm.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

//@IgnoreExtraProperties
public class Receita extends RealmObject {


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

    public Receita() {
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

    public MigrationObject migrate(){
        MigrationObject migrationObject = new MigrationObject();
        migrationObject.setId(getId());
        migrationObject.setCategoria(getCategoria());
        migrationObject.setDescricao(getDescricao());
        migrationObject.setDataVenc(getDataVenc());
        migrationObject.setValor(getValor());
        migrationObject.setIcon(getIcon());
        migrationObject.setDateRegOrUpd(getDateRegOrUpd());
        migrationObject.setTipo("Receita");
        return  migrationObject;
    }

}


