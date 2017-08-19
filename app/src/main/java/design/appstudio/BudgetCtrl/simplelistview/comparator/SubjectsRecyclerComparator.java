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

package design.appstudio.BudgetCtrl.simplelistview.comparator;

import design.appstudio.BudgetCtrl.simplelistview.recyclermodels.SubjectsRecycler;

public abstract class SubjectsRecyclerComparator implements Comparable<SubjectsRecycler> {

    public int compare(SubjectsRecycler o1, SubjectsRecycler o2) {
        int retorno = 0;
        int comparacao = o1.getMigrationObject().getTipo().compareTo(o2.getMigrationObject().getTipo());
        if (comparacao == 0) {
            int comparacao2 = o1.getMigrationObject().getCategoria().compareTo(o2.getMigrationObject().getCategoria());
            if (comparacao2 == 0) {
                retorno = o1.getMigrationObject().getDateRegOrUpd().compareTo(o2.getMigrationObject().getDateRegOrUpd());
            } else {
                retorno = comparacao2;
            }
        }
        retorno = comparacao;
        return retorno;
    }


//    int retorno = 0;
//int comparacao = o1.getMigrationObject().getTipo().compareTo(o2.getMigrationObject().getTipo());
//        if (comparacao == 0) {
//        int comparacao2 = o1.getMigrationObject().getCategoria().compareTo(o2.getMigrationObject().getCategoria());
//        if (comparacao2 == 0) {
//            retorno = o1.getMigrationObject().getDateRegOrUpd().compareTo(o2.getMigrationObject().getDateRegOrUpd());
//        } else {
//            retorno = comparacao2;
//        }
//    }
//    retorno = comparacao;
//        return retorno;
}