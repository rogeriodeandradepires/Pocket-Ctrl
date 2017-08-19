package design.appstudio.BudgetCtrl.expandablelistview.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.percent.PercentRelativeLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import design.appstudio.BudgetCtrl.MainActivity;
import design.appstudio.BudgetCtrl.R;
import design.appstudio.BudgetCtrl.customfonts.daxpro.DaxProLittleBoldTextView;
import design.appstudio.BudgetCtrl.customfonts.gotham.GothamBoldTextView;
import design.appstudio.BudgetCtrl.customfonts.gotham.GothamBookLittleBoldTextView;
import design.appstudio.BudgetCtrl.customfonts.gotham.GothamLightTextView;
import design.appstudio.BudgetCtrl.realm.model.MigrationObject;
import design.appstudio.BudgetCtrl.util.VerticalTextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private MainActivity mainActivity;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<MigrationObject>> _listDataChild;

    private SharedPreferences isGrouped;

    private String headerTitle;

    private ViewHolder holder;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<MigrationObject>> listChildData, MainActivity mainActivity) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.mainActivity = mainActivity;

        isGrouped = context.getSharedPreferences("items.grouped", Context.MODE_PRIVATE);
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String teste = "";


        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

//        Toast.makeText(_context, "getChildView", Toast.LENGTH_SHORT).show();

        final MigrationObject child = (MigrationObject) getChild(groupPosition, childPosition);

        if (convertView == null) {
            final ViewHolder holder = new ViewHolder();

            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = infalInflater.inflate(R.layout.expense_list_item, null);
            holder.headerView = convertView;
            holder.headerView.setTag(holder);
//
////            holder.holderParameter;
            holder.holderParent = parent;
//
            holder.childPosition = childPosition;
            holder.groupPosition = groupPosition;

            holder.expandableListView = (ExpandableListView) parent;

            holder.headerTitle = headerTitle;

//            holder.headerTitle = holder.expandableListView.getChildAt(0).get;
//
//            holder.edtCategorySum;
//
//            holder.buttonAddToCategory;
//            holder.buttonRemoveAllFromCategory;
//
//            holder.imgBtnHideShowCategoryItems;
//
//            //item
            holder.imgBtnEditItem = (ImageView) holder.headerView.findViewById(R.id.edit_icon);
            holder.imgBtnDeleteItem = (ImageView) holder.headerView.findViewById(R.id.delete_icon);
            holder.categoryItemIcon = (ImageView) holder.headerView.findViewById(R.id.item_category_icon);


            if (groupPosition != 0) {
                holder.categoryItemIcon.setImageDrawable(_context.getResources().getDrawable(configureIcon(holder.headerTitle)));
            }

            holder.imgBtnEditItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(_context, "editar o item=" + childPosition + "de " + holder.headerTitle, Toast.LENGTH_SHORT).show();
                }
            });

            holder.imgBtnDeleteItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(_context, "deletar o item=" + childPosition, Toast.LENGTH_SHORT).show();
                }
            });

//
            holder.rl_labelBgColor = (PercentRelativeLayout) holder.headerView.findViewById(R.id.pl_card_background);
            holder.tv_labelTextItem = (VerticalTextView) holder.headerView.findViewById(R.id.tv_register_type);
//
            holder.tv_dateRegOrUp = (GothamBookLittleBoldTextView) holder.headerView.findViewById(R.id.tv_dateRegOrUp);
//
            holder.tv_hourRegOrUp = (GothamLightTextView) holder.headerView.findViewById(R.id.tv_timeRegOrUp);
            holder.tv_itemDescription = (GothamLightTextView) holder.headerView.findViewById(R.id.txvDescription);
            holder.tv_itemValue = (GothamLightTextView) holder.headerView.findViewById(R.id.txvValor);
            holder.tv_itemDueDate = (GothamLightTextView) holder.headerView.findViewById(R.id.txvVencimentoDate);
            holder.tv_itemDueDateLabel = (GothamLightTextView) holder.headerView.findViewById(R.id.txvLblVencimento);
//
            holder.tv_itemCategory_title = (GothamBoldTextView) holder.headerView.findViewById(R.id.txvCategory);
//
            Date date = child.getDateRegOrUpd();
            SimpleDateFormat dt = new SimpleDateFormat("dd MMM");
            holder.tv_dateRegOrUp.setText(dt.format(date).toString().toUpperCase());

            if (groupPosition == 0) {
                holder.rl_labelBgColor.setBackgroundColor(_context.getResources().getColor(R.color.color1Gradient));
                holder.tv_labelTextItem.setText("Receita");
            }


            Date time = child.getDateRegOrUpd();
            SimpleDateFormat dtTime = new SimpleDateFormat("HH:mm");
            holder.tv_hourRegOrUp.setText(dtTime.format(date).toString());


//            holder.tv_itemValue.se

////
            holder.tv_itemDescription.setText(child.getDescricao());
            holder.tv_itemValue.setText(NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format((child.getValor() / 100)));
            if (groupPosition == 0) {
                holder.tv_itemDueDate.setVisibility(View.INVISIBLE);
                holder.tv_itemDueDateLabel.setVisibility(View.INVISIBLE);
            } else {
                holder.tv_itemDueDate.setText(child.getDataVenc());
            }


////
            holder.tv_itemCategory_title.setText(holder.headerTitle);

            this.holder = holder;


        } else {
            ViewHolder holder = (ViewHolder) convertView.getTag();
//            ViewHolder holder = this.holder;
            holder.holderParent = parent;
//            holder.holderParameterPosition = position;
        }

//        TextView txtListChild = (TextView) convertView
//                .findViewById(R.id.expense_icon);

//        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        headerTitle = (String) getGroup(groupPosition);
//        View v = super.getGroupView(groupPosition, isExpanded, convertView, parent);
//        final ExpandableListView mExpandableListView = (ExpandableListView) parent;
//
//        if (!mExpandableListView.isGroupExpanded(groupPosition)) {
//            mExpandableListView.expandGroup(groupPosition);
//        }

        if (convertView == null) {

            final ViewHolder holder = new ViewHolder();
            holder.expandableListView = (ExpandableListView) parent;

            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (groupPosition == 0) {
                convertView = infalInflater.inflate(R.layout.list_group_category_title_layout_null, null);

                holder.expandableListView.setGroupIndicator(null);//.addHeaderView(convertView);
//                Toast.makeText(_context, "É Zero", Toast.LENGTH_SHORT).show();

//                holder.expandableListView.getAdapter().getView(0,holder.expandableListView,holder.expandableListView).setVisibility(View.GONE);//.setGroupIndicator(null);

//                RelativeLayout.MarginLayoutParams  params = (RelativeLayout.MarginLayoutParams )convertView.getLayoutParams();
//                params.topMargin = (int) -10d; //substitute parameters for left, top, right, bottom
//                convertView.setLayoutParams(params);
//                convertView.requestLayout();

//                convertView.remo
//                convertView.set
            } else {
                convertView = infalInflater.inflate(R.layout.list_group_category_title_layout, null);
            }

//            Toast.makeText(_context, groupPosition+" "+headerTitle+"count="+getChildrenCount(groupPosition), Toast.LENGTH_SHORT).show();

            holder.rowView = convertView;
            holder.rowView.setTag(holder);

            if (!holder.expandableListView.isGroupExpanded(groupPosition)) {
                holder.expandableListView.expandGroup(groupPosition);
            }

//            holder.holderParameter;
            holder.holderParent = parent;

            holder.groupPosition = groupPosition;

            holder.headerTitle = headerTitle;

            holder.edtCategorySum = (EditText) holder.rowView.findViewById(R.id.edt_categories_value);

            holder.buttonAddToCategory = (FrameLayout) holder.rowView.findViewById(R.id.btn_edit_category);
            holder.buttonRemoveAllFromCategory = (FrameLayout) holder.rowView.findViewById(R.id.btn_edit_category);

            holder.imgBtnHideShowCategoryItems = (ImageView) holder.rowView.findViewById(R.id.btn_show_hide_category);

            holder.imgBtnHideShowCategoryItems.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (holder.expandableListView.isGroupExpanded(groupPosition)) {
                        Toast.makeText(_context, groupPosition + "isExpanded", Toast.LENGTH_SHORT).show();
                        holder.expandableListView.collapseGroup(groupPosition);

//                        mainActivity.collapseCategory(groupPosition);

                    } else {
                        Toast.makeText(_context, groupPosition + "isNOTExpanded", Toast.LENGTH_SHORT).show();
                        holder.expandableListView.expandGroup(groupPosition);
//                        mainActivity.expandCategory(groupPosition);
                    }

//                    parent.getChildAt(groupPosition)
                }
            });

            holder.buttonAddToCategory = (FrameLayout) holder.rowView.findViewById(R.id.btn_edit_category);
            holder.buttonRemoveAllFromCategory = (FrameLayout) holder.rowView.findViewById(R.id.btn_delete_category);


            holder.buttonAddToCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(_context, "addTO=" + holder.headerTitle, Toast.LENGTH_SHORT).show();

                }
            });

            holder.buttonRemoveAllFromCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(_context, "deleteFrom=" + holder.headerTitle, Toast.LENGTH_SHORT).show();

                }
            });

//            //item
//            holder.imgBtnEditItem = (ImageButton) holder.rowView.findViewById(R.id.btn_edit_category);
//            holder.imgBtnDeleteItem = (ImageView) holder.rowView.findViewById(R.id.btn_edit_category);
//            holder.categoryItemIcon = (ImageView) holder.rowView.findViewById(R.id.btn_edit_category);
//
//            holder.rl_labelBgColor = (FrameLayout) holder.rowView.findViewById(R.id.btn_edit_category)
//            holder.tv_labelTextItem = (FrameLayout) holder.rowView.findViewById(R.id.btn_edit_category)
//
//            holder.tv_dateRegOrUp = (FrameLayout) holder.rowView.findViewById(R.id.btn_edit_category)
//
//            holder.tv_hourRegOrUp = (FrameLayout) holder.rowView.findViewById(R.id.btn_edit_category)
//            holder.tv_itemDescription = (FrameLayout) holder.rowView.findViewById(R.id.btn_edit_category)
//            holder.tv_itemValue = (FrameLayout) holder.rowView.findViewById(R.id.btn_edit_category)
//            holder.tv_itemDueDate = (FrameLayout) holder.rowView.findViewById(R.id.btn_edit_category)
//
//            holder.tv_itemCategory_title = (FrameLayout) holder.rowView.findViewById(R.id.btn_edit_category)

            if (isGrouped.getBoolean("isGrouped", false)) {
                holder.tv_category_title = (DaxProLittleBoldTextView) holder.rowView.findViewById(R.id.lbl_category);
                holder.tv_category_title.setText(holder.headerTitle);
                //se não estiver agrupado, seta o título de orçamento como nulo
            } else {
                holder.rowView.setVisibility(View.GONE);
            }
        } else {

//            Toast.makeText(_context, "Não era nula="+groupPosition, Toast.LENGTH_SHORT).show();
//
            final ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.holderParent = parent;

//            holder.expandableListView = (ExpandableListView) parent;
//
//            holder.imgBtnHideShowCategoryItems.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    if (holder.expandableListView.isGroupExpanded(groupPosition)) {
////                        Toast.makeText(_context,groupPosition+"isExpanded", Toast.LENGTH_SHORT).show();
//                        holder.expandableListView.collapseGroup(groupPosition);
//
////                        mainActivity.collapseCategory(groupPosition);
//
//                    }else{
////                        Toast.makeText(_context,groupPosition+"isNOTExpanded", Toast.LENGTH_SHORT).show();
//                        holder.expandableListView.expandGroup(groupPosition);
////                        mainActivity.expandCategory(groupPosition);
//                    }
//
////                    parent.getChildAt(groupPosition)
//                }
//            });

//            holder.expandableListView = (ExpandableListView) parent;
//
//            if (!holder.expandableListView.isGroupExpanded(groupPosition)) {
//                holder.expandableListView.expandGroup(groupPosition);
//            }

//            holder.holderParameterPosition = position;
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    public class ViewHolder {

        //-----
        //header

        ExpandableListView expandableListView;

        MigrationObject migrationObject;

        String headerTitle;

        EditText edtCategorySum;

        FrameLayout buttonAddToCategory;
        FrameLayout buttonRemoveAllFromCategory;

        ImageView imgBtnHideShowCategoryItems;
        //-----

        //-----
        //item
        ImageView imgBtnEditItem;
        ImageView imgBtnDeleteItem;
        ImageView categoryItemIcon;

        PercentRelativeLayout rl_labelBgColor;
        VerticalTextView tv_labelTextItem;

        GothamBookLittleBoldTextView tv_dateRegOrUp;

        GothamLightTextView tv_hourRegOrUp;
        GothamLightTextView tv_itemDescription;
        GothamLightTextView tv_itemValue;
        GothamLightTextView tv_itemDueDate;
        GothamLightTextView tv_itemDueDateLabel;

        GothamBoldTextView tv_itemCategory_title;

        DaxProLittleBoldTextView tv_category_title;

        //-----
//        ViewHolder holderParameter;
        ViewGroup holderParent;

        int childPosition;
        int groupPosition;

        View rowView;
        View headerView;

        public ViewHolder() {

        }
    }

    public int configureIcon(String category) {

//        Toast.makeText(_context, category, Toast.LENGTH_SHORT).show();
        int icon = 0;

        switch (category) {
            case "Água": {
                icon = R.drawable.ic_svg_cat_water_bill;
                break;
            }
            case "Cartão": {
                icon = R.drawable.ic_svg_cat_credit_card2;
                break;
            }
            case "Casa": {
                icon = R.drawable.ic_svg_cat_casa;
                break;
            }
            case "Combustível": {
                icon = R.drawable.ic_svg_cat_fuel;
                break;
            }
            case "Contas": {
                icon = R.drawable.ic_svg_cat_bill;
                break;
            }
            case "Cosméticos": {
                icon = R.drawable.ic_svg_cat_cosmetics;
                break;
            }
            case "Dependentes": {
                icon = R.drawable.ic_svg_cat_dependent;
                break;
            }
            case "Diversão": {
                icon = R.drawable.ic_svg_cat_leisure;
                break;
            }
            case "Educação": {
                icon = R.drawable.ic_svg_cat_education;
                break;
            }
            case "Eletrônicos": {
                icon = R.drawable.ic_svg_cat_eletronicos;
                break;
            }
            case "Empréstimo": {
                icon = R.drawable.ic_svg_cat_loan;
                break;
            }
            case "Energia": {
                icon = R.drawable.ic_svg_cat_energy;
                break;
            }
            case "Esportes": {
                icon = R.drawable.ic_svg_cat_sports;
                break;
            }
            case "Mercado": {
                icon = R.drawable.ic_svg_cat_supermercado;
                break;
            }
            case "Pets": {
                icon = R.drawable.ic_svg_cat_pets;
                break;
            }
            case "Poupança": {
                icon = R.drawable.ic_svg_cat_money_saving;
                break;
            }
            case "Presente": {
                icon = R.drawable.ic_svg_cat_gift;
                break;
            }
            case "Salário": {
                icon = R.drawable.ic_svg_salary;
                break;
            }
            case "Saúde": {
                icon = R.drawable.ic_svg_cat_health;
                break;
            }
            case "Serviços": {
                icon = R.drawable.ic_svg_cat_servicos;
                break;
            }
            case "Transporte": {
                icon = R.drawable.ic_svg_cat_transport;
                break;
            }
            case "Vestuário": {
                icon = R.drawable.ic_svg_cat_vestuario;
                break;
            }
            case "Viagem": {
                icon = R.drawable.ic_svg_cat_travel;
                break;
            }
            case "Outros": {
                icon = R.drawable.ic_svg_cat_outros;
                break;
            }
            default: {
                break;
            }
        }


        return icon;
    }

}