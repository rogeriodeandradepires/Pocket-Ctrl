/*
Copyright 2016 StepStone Services

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
/*
Copyright 2016 StepStone Services

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package design.appstudio.BudgetCtrl.simplelistview.recyclermodels;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.List;

import design.appstudio.BudgetCtrl.realm.model.MigrationObject;
import io.realm.Realm;

public class SubjectsRecycler {//implements Comparable<SubjectsRecycler> {
    private int categoriaPosition;

    private String categoria;
    private int itemPosition;

    private View layout_item_view;
    private ImageButton buttonParameter;
    private LinearLayout.LayoutParams layoutParams;
    private List<String> listaProvisoria;
    private List<String> materiasSemestre;

    private boolean isCategoryHeader = false;

    private String headerTitle;

    private Realm realm;

    private MigrationObject migrationObject;

//    public SubjectsRecycler(int term, int subject) {
//        this.setTerm(term);
//        this.setSubject(subject);
//    }

    public SubjectsRecycler() {
    }

    public ImageButton getButtonParameter() {
        return buttonParameter;
    }

    public void setButtonParameter(ImageButton buttonParameter) {
        this.buttonParameter = buttonParameter;
    }

    public LinearLayout.LayoutParams getLayoutParams() {
        return layoutParams;
    }

    public void setLayoutParams(LinearLayout.LayoutParams layoutParams) {
        this.layoutParams = layoutParams;
    }

    public List<String> getListaProvisoria() {
        return listaProvisoria;
    }

    public void setListaProvisoria(List<String> listaProvisoria) {
        this.listaProvisoria = listaProvisoria;
    }

    public List<String> getMateriasSemestre() {
        return materiasSemestre;
    }

    public void setMateriasSemestre(List<String> materiasSemestre) {
        this.materiasSemestre = materiasSemestre;
    }

    public Realm getRealm() {
        return realm;
    }

    public void setRealm(Realm realm) {
        this.realm = realm;
    }

    public int getCategoriaPosition() {
        return categoriaPosition;
    }

    public void setCategoriaPosition(int categoriaPosition) {
        this.categoriaPosition = categoriaPosition;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getItemPosition() {
        return itemPosition;
    }

    public void setItemPosition(int itemPosition) {
        this.itemPosition = itemPosition;
    }

    public View getLayout_item_view() {
        return layout_item_view;
    }

    public void setLayout_item_view(View layout_item_view) {
        this.layout_item_view = layout_item_view;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public MigrationObject getMigrationObject() {
        return migrationObject;
    }

    public void setMigrationObject(MigrationObject migrationObject) {
        this.migrationObject = migrationObject;
    }

//    @Override
//    public int compareTo(@NonNull SubjectsRecycler o1){//}, @NonNull SubjectsRecycler o2) {
////        return getCategoria().compareTo(o.getCategoria());
//        int retorno = 0;
//        int comparacao = o1.getMigrationObject().getTipo().compareTo(o2.getMigrationObject().getTipo());
//        if (comparacao == 0) {
//            int comparacao2 = o1.getMigrationObject().getCategoria().compareTo(o2.getMigrationObject().getCategoria());
//            if (comparacao2 == 0) {
//                retorno = o1.getMigrationObject().getDateRegOrUpd().compareTo(o2.getMigrationObject().getDateRegOrUpd());
//            } else {
//                retorno = comparacao2;
//            }
//        }
//        retorno = comparacao;
//        return retorno;
//    }


    public boolean isCategoryHeader() {
        return isCategoryHeader;
    }

    public void setCategoryHeader(boolean categoryHeader) {
        isCategoryHeader = categoryHeader;
    }
}
