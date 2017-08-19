package design.appstudio.BudgetCtrl.simplelistview.adapter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.percent.PercentRelativeLayout;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import design.appstudio.BudgetCtrl.MainActivity;
import design.appstudio.BudgetCtrl.R;
import design.appstudio.BudgetCtrl.customfonts.daxpro.DaxProLittleBoldTextView;
import design.appstudio.BudgetCtrl.customfonts.gotham.GothamBoldTextView;
import design.appstudio.BudgetCtrl.customfonts.gotham.GothamBookLittleBoldTextView;
import design.appstudio.BudgetCtrl.customfonts.gotham.GothamLightTextView;
import design.appstudio.BudgetCtrl.realm.model.Despesa;
import design.appstudio.BudgetCtrl.realm.model.MigrationObject;
import design.appstudio.BudgetCtrl.realm.model.Receita;
import design.appstudio.BudgetCtrl.simplelistview.recyclermodels.SubjectsRecycler;
import design.appstudio.BudgetCtrl.util.VerticalTextView;

import static android.R.attr.type;

public class SimpleListAdapter extends BaseAdapter implements DatePickerDialog.OnDateSetListener {

    private List<SubjectsRecycler> subjectsRecyclers;

    private SweetAlertDialog sweetAlertDialog;
    private boolean limit = false;
    private Long parametroPTodasAsDisciplinas;
    private int cursoGlobal;

    private MainActivity activity;

    private Context _context;

    private RelativeLayout rl_form;
    private TextInputEditText sa_edt_form_category;
    private TextInputEditText sa_edt_form_description;
    private TextInputEditText sa_edt_form_value;
    private TextInputEditText sa_edt_form_date;

    private DatePickerDialog datePickerDialog;

    private String cleanValue;

    public SimpleListAdapter(Context context, final List<SubjectsRecycler> subjectsRecyclers, MainActivity act) {
        activity = act;
        this.subjectsRecyclers = subjectsRecyclers;
//        this.parametroPTodasAsDisciplinas = parametroTdsAsDisciplinas;
        this._context = context;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        int retorno = 0;
//        retorno = (nomeLista.equals("terms")) ? mListStringTerms.size() : mListStringSubjects.size();
        return (subjectsRecyclers == null ? 0 : subjectsRecyclers.size());//retorno;
    }

    @Override
    public SubjectsRecycler getItem(int position) {
        SubjectsRecycler retorno = null;
        retorno = subjectsRecyclers.get(position);
        return retorno;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder holder;

//        if (convertView == null) {
        holder = new ViewHolder();
        LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infalInflater.inflate(R.layout.expense_simplelist_item, null);
        holder.rowView = convertView;
        holder.rowView.setTag(holder);

        holder.isForShowing = _context.getSharedPreferences("is.forshowing", Context.MODE_PRIVATE);

        holder.headerView = (RelativeLayout) holder.rowView.findViewById(R.id.tl_item_title_bar);

        holder.migrationObject = getItem(position).getMigrationObject();

        List<SubjectsRecycler> subjectsRecyclerList = new ArrayList<>();

        for (SubjectsRecycler subjectsRecycler : this.subjectsRecyclers) {
            if (subjectsRecycler.getCategoria().equals(holder.migrationObject)) {
                subjectsRecyclerList.add(subjectsRecycler);
            }
        }

        if (!activity.isGroupedBoolean()) {
            holder.headerView.setVisibility(View.GONE);
        }
//            holder.headerView.setVisibility(View.GONE);

        if (!getItem(position).isCategoryHeader()) {
            holder.headerView.setVisibility(View.GONE);
        }

//
////            holder.holderParameter;
        holder.holderParent = parent;
        activity.setHolderParent(parent);

        holder.headerTitle = holder.migrationObject.getCategoria();
//
        holder.childPosition = position;
//            holder.groupPosition = groupPosition;

        holder.listView = (ListView) parent;

//            holder.headerTitle = headerTitle;

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
        holder.imgBtnEditItem = (ImageView) holder.rowView.findViewById(R.id.edit_icon);
        holder.imgBtnDeleteItem = (ImageView) holder.rowView.findViewById(R.id.delete_icon);
        holder.categoryItemIcon = (ImageView) holder.rowView.findViewById(R.id.item_category_icon);
//
        holder.imgBtnHideShowCategoryItems = (ImageView) holder.rowView.findViewById(R.id.btn_show_hide_category);
        holder.tv_category_title = (DaxProLittleBoldTextView) holder.rowView.findViewById(R.id.lbl_category);
        holder.tv_category_title.setText(holder.headerTitle);

        holder.edtCategorySum = (EditText) holder.rowView.findViewById(R.id.edt_categories_value);
        holder.edtCategoryMask = (EditText) holder.rowView.findViewById(R.id.edt_category_mask);

        holder.buttonAddToCategory = (FrameLayout) holder.rowView.findViewById(R.id.btn_edit_category);
        holder.buttonRemoveAllFromCategory = (FrameLayout) holder.rowView.findViewById(R.id.btn_edit_category);

        holder.imgBtnHideShowCategoryItems = (ImageView) holder.rowView.findViewById(R.id.btn_show_hide_category);

        holder.buttonAddToCategory = (FrameLayout) holder.rowView.findViewById(R.id.btn_edit_category);
        holder.buttonRemoveAllFromCategory = (FrameLayout) holder.rowView.findViewById(R.id.btn_delete_category);

        holder.rl_labelBgColor = (PercentRelativeLayout) holder.rowView.findViewById(R.id.pl_card_background);
        holder.tv_labelTextItem = (VerticalTextView) holder.rowView.findViewById(R.id.tv_register_type);
//
        holder.tv_dateRegOrUp = (GothamBookLittleBoldTextView) holder.rowView.findViewById(R.id.tv_dateRegOrUp);
//
        holder.tv_hourRegOrUp = (GothamLightTextView) holder.rowView.findViewById(R.id.tv_timeRegOrUp);
        holder.tv_itemDescription = (GothamLightTextView) holder.rowView.findViewById(R.id.txvDescription);
        holder.tv_itemValue = (GothamLightTextView) holder.rowView.findViewById(R.id.txvValor);
        holder.tv_itemDueDate = (GothamLightTextView) holder.rowView.findViewById(R.id.txvVencimentoDate);
        holder.tv_itemDueDateLabel = (GothamLightTextView) holder.rowView.findViewById(R.id.txvLblVencimento);
//
        holder.tv_itemCategory_title = (GothamBoldTextView) holder.rowView.findViewById(R.id.txvCategory);

        holder.imgBtnHideShowCategoryItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.rl_labelBgColor.getVisibility() == View.VISIBLE) {
//                        holder.rl_labelBgColor.setVisibility(View.GONE);

                    for (int i = 0; i < holder.holderParent.getChildCount(); i++) {
                        if (((ViewHolder) holder.holderParent.getChildAt(i).getTag()).migrationObject.getTipo().equals(holder.migrationObject.getTipo())) {
                            if (((ViewHolder) holder.holderParent.getChildAt(i).getTag()).migrationObject.getCategoria().equals(holder.migrationObject.getCategoria())) {
                                ((ViewHolder) holder.holderParent.getChildAt(i).getTag()).rl_labelBgColor.setVisibility(View.GONE);
                                holder.isForShowing.edit().putBoolean(String.valueOf(((ViewHolder) holder.holderParent.getChildAt(i).getTag()).migrationObject.getId()),false).apply();
//                                ((ViewHolder) activity.getListAdapter().getView(i, null, activity.getListView()).getTag()).rl_labelBgColor.setVisibility(View.GONE);
//                                Toast.makeText(activity, "clicou p esconder", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                } else {
                    holder.rl_labelBgColor.setVisibility(View.VISIBLE);
                    for (int i = 0; i < holder.holderParent.getChildCount(); i++) {
                        if (((ViewHolder) holder.holderParent.getChildAt(i).getTag()).migrationObject.getTipo().equals(holder.migrationObject.getTipo())) {
                            if (((ViewHolder) holder.holderParent.getChildAt(i).getTag()).migrationObject.getCategoria().equals(holder.migrationObject.getCategoria())) {
                                ((ViewHolder) holder.holderParent.getChildAt(i).getTag()).rl_labelBgColor.setVisibility(View.VISIBLE);
                                holder.isForShowing.edit().putBoolean(String.valueOf(((ViewHolder) holder.holderParent.getChildAt(i).getTag()).migrationObject.getId()),true).apply();
//                                ((ViewHolder) activity.getListAdapter().getView(i, null, activity.getListView()).getTag()).rl_labelBgColor.setVisibility(View.VISIBLE);
//                                Toast.makeText(activity, "clicou p mostrar", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        });

        if (!holder.isForShowing.getBoolean(String.valueOf(holder.migrationObject.getId()),true)) {
            if (activity.isGroupedBoolean()) {
                holder.rl_labelBgColor.setVisibility(View.GONE);
            }else{

            }
        }else{
            holder.rl_labelBgColor.setVisibility(View.VISIBLE);
        }

        holder.buttonAddToCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemTOCategory(holder.migrationObject);

            }
        });

        holder.buttonRemoveAllFromCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.removeAllFromCategory(holder.headerTitle, holder.migrationObject.getTipo());

            }
        });

        holder.edtCategorySum.setText(activity.categorySum(holder.headerTitle, holder.migrationObject.getTipo()));

        holder.categoryItemIcon.setImageDrawable(_context.getResources().getDrawable(configureIcon(holder.migrationObject.getCategoria())));
        holder.edtCategoryMask.setCompoundDrawablesWithIntrinsicBounds((holder.migrationObject.getTipo().equals("Receita")?R.drawable.ic_svg_revenue_green:R.drawable.ic_svg_expense_red),0, 0, 0);

        holder.imgBtnEditItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    Toast.makeText(_context, "editar o item=" + holder.childPosition + "de " + holder.headerTitle, Toast.LENGTH_SHORT).show();
                editItem(holder.migrationObject, holder.holderParent, holder.rowView);
            }
        });

        holder.imgBtnDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    Toast.makeText(_context, "deletar o item=" + holder.childPosition, Toast.LENGTH_SHORT).show();

                removeItem(holder.migrationObject, holder.holderParent, holder.rowView);

            }
        });

        if (holder.migrationObject.getTipo().equals("Receita")) {
            holder.headerView.setBackgroundResource(R.drawable.bg_balance_green_gradienttoolbar);

//                holder.edtCategoryMask.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_svg_revenue_green, 0, 0, 0);

            holder.rl_labelBgColor.setBackgroundColor(_context.getResources().getColor(R.color.color1Gradient));
            holder.tv_labelTextItem.setText("Receita");
            holder.tv_itemDueDateLabel.setVisibility(View.INVISIBLE);
            holder.tv_itemDueDate.setVisibility(View.INVISIBLE);
        } else {
            holder.tv_itemDueDate.setText(holder.migrationObject.getDataVenc());
        }
//
        Date date = holder.migrationObject.getDateRegOrUpd();
        SimpleDateFormat dt = new SimpleDateFormat("dd MMM");
        holder.tv_dateRegOrUp.setText(dt.format(date).toString().toUpperCase());

        Date time = holder.migrationObject.getDateRegOrUpd();
        SimpleDateFormat dtTime = new SimpleDateFormat("HH:mm");
        holder.tv_hourRegOrUp.setText(dtTime.format(date).toString());


//            holder.tv_itemValue.se

////
        holder.tv_itemDescription.setText(holder.migrationObject.getDescricao());
        holder.tv_itemValue.setText(NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format((holder.migrationObject.getValor() / 100)));

////
        holder.tv_itemCategory_title.setText(holder.headerTitle);

//        } else {
//            holder = (ViewHolder) convertView.getTag();
//            holder.holderParent = parent;
////            holder.holderParameterPosition = position;
//        }

        return holder.rowView;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month = month + 1;
        String dataString = String.valueOf(dayOfMonth > 9 ? dayOfMonth : "0" + dayOfMonth);
        dataString += (month > 9 ? month : "0" + month);
        dataString += year;

        this.sa_edt_form_date.setText(dataString);
    }

    public class ViewHolder {


        // ViewHolder holderParameter;
        ViewGroup holderParent;

        int childPosition;
        int groupPosition;

        SharedPreferences isForShowing;

        View rowView;
        RelativeLayout headerView;

        //-----
        //header

        ListView listView;

        MigrationObject migrationObject;

        String headerTitle;

        EditText edtCategorySum;
        EditText edtCategoryMask;

        FrameLayout buttonAddToCategory;
        FrameLayout buttonRemoveAllFromCategory;

        ImageView imgBtnHideShowCategoryItems;

        ImageView categoryTitleIcon;

        DaxProLittleBoldTextView tv_category_title;
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

        //-----

        public ViewHolder() {

        }
    }

    public int configureIcon(String category) {

//        Toast.makeText(_context, category, Toast.LENGTH_SHORT).show();
        int icon = 0;

        switch (category) {
            case "Água": {
                icon = R.drawable.ic_svg_cat_water_bill_bigger;
                break;
            }
            case "Cartão": {
                icon = R.drawable.ic_svg_cat_credit_card2_bigger;
                break;
            }
            case "Casa": {
                icon = R.drawable.ic_svg_cat_casa_bigger;
                break;
            }
            case "Combustível": {
                icon = R.drawable.ic_svg_cat_fuel_bigger;
                break;
            }
            case "Contas": {
                icon = R.drawable.ic_svg_cat_bill_bigger;
                break;
            }
            case "Cosméticos": {
                icon = R.drawable.ic_svg_cat_cosmetics_bigger;
                break;
            }
            case "Dependentes": {
                icon = R.drawable.ic_svg_cat_dependent_bigger;
                break;
            }
            case "Diversão": {
                icon = R.drawable.ic_svg_cat_leisure_bigger;
                break;
            }
            case "Educação": {
                icon = R.drawable.ic_svg_cat_education_bigger;
                break;
            }
            case "Eletrônicos": {
                icon = R.drawable.ic_svg_cat_eletronicos_bigger;
                break;
            }
            case "Empréstimo": {
                icon = R.drawable.ic_svg_cat_loan_bigger;
                break;
            }
            case "Energia": {
                icon = R.drawable.ic_svg_cat_energy_bigger;
                break;
            }
            case "Esportes": {
                icon = R.drawable.ic_svg_cat_sports_bigger;
                break;
            }
            case "Mercado": {
                icon = R.drawable.ic_svg_cat_supermercado_bigger;
                break;
            }
            case "Pets": {
                icon = R.drawable.ic_svg_cat_pets_bigger;
                break;
            }
            case "Poupança": {
                icon = R.drawable.ic_svg_cat_money_saving_bigger;
                break;
            }
            case "Presente": {
                icon = R.drawable.ic_svg_cat_gift_bigger;
                break;
            }
            case "Salário": {
                icon = R.drawable.ic_svg_salary_bigger;
                break;
            }
            case "Saúde": {
                icon = R.drawable.ic_svg_cat_health_bigger;
                break;
            }
            case "Serviços": {
                icon = R.drawable.ic_svg_cat_servicos_bigger;
                break;
            }
            case "Transporte": {
                icon = R.drawable.ic_svg_cat_transport_bigger;
                break;
            }
            case "Vestuário": {
                icon = R.drawable.ic_svg_cat_vestuario_bigger;
                break;
            }
            case "Viagem": {
                icon = R.drawable.ic_svg_cat_travel_bigger;
                break;
            }
            case "Outros": {
                icon = R.drawable.ic_svg_cat_outros_bigger;
                break;
            }
            default: {
                break;
            }
        }
        return icon;
    }


    public void removeItem(final MigrationObject migrationObject, final ViewGroup holderParent, final View holderView) {
        sweetAlertDialog = new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Excluir " + migrationObject.getTipo())
                .cleanWheelData()
                .setContentText("Tem certeza que quer excluir esta " + migrationObject.getTipo() + "?")
                .setCancelText("Não")
                .setConfirmText("Sim")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        if (!activity.getRealm().isInTransaction()) {
                            activity.getRealm().beginTransaction();
                        }

                        activity.getRealm().where((migrationObject.getTipo().equals("Despesa") ? Despesa.class : Receita.class)).equalTo("id", migrationObject.getId()).findFirst().deleteFromRealm();

                        activity.getRealm().commitTransaction();
//                        prepareListData();

                        activity.successMessage("Excluído", " Esta " + migrationObject.getTipo() + " foi excluída com sucesso!", sDialog);

                        List<SubjectsRecycler> listSubjectsForRemoving = new ArrayList<>();

                        listSubjectsForRemoving.addAll(subjectsRecyclers);

                        for (SubjectsRecycler subjectsRecycler : subjectsRecyclers) {
                            if (subjectsRecycler.getMigrationObject().getId() == migrationObject.getId()) {
                                listSubjectsForRemoving.remove(subjectsRecycler);
                            }
                        }

                        subjectsRecyclers = listSubjectsForRemoving;

                        activity.setTodasAsReceitas(activity.getRealm().where(Receita.class).findAll());
                        activity.setTodasAsDespesas(activity.getRealm().where(Despesa.class).findAll());

                        holderParent.removeViewInLayout(holderView);

                        activity.getListAdapter().notifyDataSetChanged();
                        activity.calculaValores();
//                        subjectsRecyclers.get()


                        //15/08/2017
//                        notifyDataSet
                    }
                });

        if (!sweetAlertDialog.isShowing()) {
            sweetAlertDialog.show();
        }
    }

    public void editItem(final MigrationObject migrationObject, final ViewGroup holderParent, final View holderView) {
        sweetAlertDialog = new SweetAlertDialog(activity, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setCustomImage(migrationObject.getTipo().equals("Despesa") ? R.drawable.ic_svg_expense_red : R.drawable.ic_svg_revenue_green)
                .setTitleText("Editar " + migrationObject.getTipo())
                .showContentText(false)
                .setContentText("")
                .setCancelText("Cancelar")
                .setConfirmText("Salvar")
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        if (cleanValue != null) {
                            if (Double.valueOf(cleanValue) > 0) {
                                if (migrationObject.getTipo().equals("Despesa")) {
                                    Despesa despesa = activity.getRealm().where(Despesa.class).equalTo("id", migrationObject.getId()).findFirst();

                                    if (!activity.getRealm().isInTransaction()) {
                                        activity.getRealm().beginTransaction();
                                    }

                                    despesa.setCategoria(sa_edt_form_category.getText().toString());

                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                    Date date = new Date();

                                    if (sa_edt_form_date.getText() == null) {

                                        sa_edt_form_date.setText(String.valueOf(sdf.format(date)));
                                    } else {
                                        if (sa_edt_form_date.getText().toString().equals("")) {
                                            sa_edt_form_date.setText(String.valueOf(sdf.format(date)));
                                        }
                                    }

                                    despesa.setDataVenc(sa_edt_form_date.getText().toString());

                                    despesa.setDescricao(sa_edt_form_description.getText().toString());
                                    despesa.setValor(Double.valueOf(cleanValue));

                                    despesa.setDateRegOrUpd(new Date());

                                    activity.getRealm().copyToRealmOrUpdate(despesa);

                                    activity.getRealm().commitTransaction();

                                    activity.setTodasAsReceitas(activity.getRealm().where(Receita.class).findAll());
                                    activity.setTodasAsDespesas(activity.getRealm().where(Despesa.class).findAll());

                                    List<SubjectsRecycler> listSubjectsForRemoving = new ArrayList<>();
                                    SubjectsRecycler subjectsRecyclerParameter = new SubjectsRecycler();

                                    for (SubjectsRecycler subjectsRecycler : subjectsRecyclers) {
                                        if (subjectsRecycler.getMigrationObject().getId() == migrationObject.getId()) {
                                            subjectsRecyclerParameter = subjectsRecycler;
                                        }
                                    }

                                    SubjectsRecycler subject = new SubjectsRecycler();

                                    subject.setCategoria(despesa.getCategoria());

                                    subject.setHeaderTitle(despesa.getCategoria());

                                    subject.setMigrationObject(despesa.migrate());

                                    subject.setRealm(subjectsRecyclerParameter.getRealm());

                                    subjectsRecyclers.set(subjectsRecyclers.indexOf(subjectsRecyclerParameter), subject);

                                    holderParent.removeAllViewsInLayout();

                                    activity.getListAdapter().notifyDataSetChanged();

                                    activity.calculaValores();

                                } else {
                                    Receita receita = activity.getRealm().where(Receita.class).equalTo("id", migrationObject.getId()).findFirst();

                                    if (!activity.getRealm().isInTransaction()) {
                                        activity.getRealm().beginTransaction();
                                    }

                                    receita.setCategoria(sa_edt_form_category.getText().toString());

                                    receita.setDescricao(sa_edt_form_description.getText().toString());
                                    receita.setValor(Double.valueOf(cleanValue));

                                    receita.setDateRegOrUpd(new Date());

                                    activity.getRealm().copyToRealmOrUpdate(receita);

                                    activity.getRealm().commitTransaction();

                                    activity.setTodasAsReceitas(activity.getRealm().where(Receita.class).findAll());
                                    activity.setTodasAsDespesas(activity.getRealm().where(Despesa.class).findAll());

                                    List<SubjectsRecycler> listSubjectsForRemoving = new ArrayList<>();
                                    SubjectsRecycler subjectsRecyclerParameter = new SubjectsRecycler();

                                    for (SubjectsRecycler subjectsRecycler : subjectsRecyclers) {
                                        if (subjectsRecycler.getMigrationObject().getId() == migrationObject.getId()) {
                                            subjectsRecyclerParameter = subjectsRecycler;
                                        }
                                    }

                                    SubjectsRecycler subject = new SubjectsRecycler();

                                    subject.setCategoria(receita.getCategoria());

                                    subject.setHeaderTitle(receita.getCategoria());

                                    subject.setMigrationObject(receita.migrate());

                                    subject.setRealm(subjectsRecyclerParameter.getRealm());

                                    subjectsRecyclers.set(subjectsRecyclers.indexOf(subjectsRecyclerParameter), subject);

                                    holderParent.removeAllViewsInLayout();

                                    activity.getListAdapter().notifyDataSetChanged();

                                    activity.calculaValores();
                                }

                                sDialog.dismiss();
                            } else {


                            }

                        } else {
                            Toast.makeText(activity, "Por favor, preencha um valor para esta " + type, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        if (!sweetAlertDialog.isShowing()) {
            sweetAlertDialog.show();

            sweetAlertDialog.changeFormVisibility();

            this.sa_edt_form_category = sweetAlertDialog.getEdt_form_category();
            this.sa_edt_form_description = sweetAlertDialog.getEdt_form_description();
            this.sa_edt_form_value = sweetAlertDialog.getEdt_form_value();
            this.sa_edt_form_date = sweetAlertDialog.getEdt_form_date();

            long date = System.currentTimeMillis();

            SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
            int dateStringYear = Integer.valueOf(sdfYear.format(date));
            SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
            int dateStringMonth = Integer.valueOf(sdfMonth.format(new Date())) - 1;
            SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
            int dateStringDay = Integer.valueOf(sdfDay.format(date));

            datePickerDialog = new DatePickerDialog(_context, this, dateStringYear, dateStringMonth, dateStringDay);
            datePickerDialog.getDatePicker().setMinDate(new Date().getTime());

            sa_edt_form_date.addTextChangedListener(formatVencimento());

            sa_edt_form_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
//                    rl_form.requestFocus();
                        hideSoftKeyboard(activity);
                        datePickerDialog.show();

                        datePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                rl_form.requestFocus();
                            }
                        });

                    }
                }
            });

            sa_edt_form_value.addTextChangedListener(formatValue());

            sa_edt_form_category.setCompoundDrawablesWithIntrinsicBounds(Integer.valueOf(configureIcon(migrationObject.getCategoria())), 0, 0, 0);

            double valor;
//            String paraConverter;

            if (migrationObject.getTipo().equals("Receita")) {
                valor = activity.getRealm().where(Receita.class).equalTo("id", migrationObject.getId()).findFirst().getValor() / 100;
            } else {
                valor = activity.getRealm().where(Despesa.class).equalTo("id", migrationObject.getId()).findFirst().getValor() / 100;
            }

            sa_edt_form_category.setText(migrationObject.getCategoria());
            sa_edt_form_description.setText(migrationObject.getDescricao());
            sa_edt_form_value.setText(NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format((migrationObject.getValor() / 100)));
            sa_edt_form_date.setText(migrationObject.getDataVenc());

            if (migrationObject.getTipo().equals("Receita")) {
                sa_edt_form_date.setVisibility(View.GONE);
            }

            rl_form = sweetAlertDialog.getRl_form();
        }
    }

    public void hideSoftKeyboard(Activity activity) {
        rl_form.clearFocus();
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                rl_form.getWindowToken(), 0);
    }

    public TextWatcher formatValue() {
        TextWatcher retorno = new TextWatcher() {
            private String current = "";

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals(current)) {
                    sa_edt_form_value.removeTextChangedListener(this);

                    cleanValue = charSequence.toString().replaceAll("[R$.,]", "");

                    if (cleanValue.length() != 1) {
                        cleanValue = cleanValue.replaceAll("[^\\d]", "");
                    } else {
                        cleanValue = cleanValue.replaceAll("[^\\d]", "0");
                    }

                    double parsed = Double.parseDouble(cleanValue);
                    String formatted = NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format((parsed / 100));

                    formatted = formatted.replace("R$", "R$ ");

                    current = formatted;
                    sa_edt_form_value.setText(formatted);
                    sa_edt_form_value.setSelection(formatted.length());

                    sa_edt_form_value.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }


        };

        return retorno;

    }

    public TextWatcher formatVencimento() {
        TextWatcher retorno = new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "DDMMAAAA";
            private Calendar cal = Calendar.getInstance();


            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals(current)) {
                    String clean = charSequence.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int g = 2; g <= cl && g < 6; g += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8) {
                        clean = clean + ddmmyyyy.substring(clean.length());
                    } else {
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day = Integer.parseInt(clean.substring(0, 2));
                        int mon = Integer.parseInt(clean.substring(2, 4));
                        int year = Integer.parseInt(clean.substring(4, 8));

                        if (mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon - 1);
                        year = (year < 1900) ? 1900 : (year > 2100) ? 2100 : year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
                        clean = String.format("%02d%02d%02d", day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    sa_edt_form_date.setText(current);
                    sa_edt_form_date.setSelection(sel < current.length() ? sel : current.length());

//
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }


        };

        return retorno;

    }

    public void addItemTOCategory(final MigrationObject migrationObject) {
        sweetAlertDialog = new SweetAlertDialog(_context, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setCustomImage(migrationObject.getTipo().equals("Despesa") ? R.drawable.ic_svg_expense_red : R.drawable.ic_svg_revenue_green)
                .setTitleText("Adicionar " + migrationObject.getTipo())
                .showContentText(false)
                .setContentText("")
                .setCancelText("Cancelar")
                .setConfirmText("Salvar")
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        if (cleanValue != null) {
                                if (Double.valueOf(cleanValue) > 0) {

                                if (migrationObject.getTipo().equals("Despesa")) {
                                    Despesa despesa = new Despesa();
                                    despesa.setId(activity.getUniqueId());

                                    if (!activity.getRealm().isInTransaction()) {
                                        activity.getRealm().beginTransaction();
                                    }

                                    despesa.setCategoria(migrationObject.getCategoria());

                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                    Date date = new Date();

                                    if (sa_edt_form_date.getText() == null) {
                                        sa_edt_form_date.setText(String.valueOf(sdf.format(date)));
                                    } else {
                                        if (sa_edt_form_date.getText().toString().equals("")) {
                                            sa_edt_form_date.setText(String.valueOf(sdf.format(date)));
                                        }
                                    }

                                    despesa.setDataVenc(sa_edt_form_date.getText().toString());

                                    despesa.setDescricao(sa_edt_form_description.getText().toString());
                                    despesa.setValor(Double.valueOf(cleanValue));

                                    despesa.setDateRegOrUpd(new Date());

                                    activity.getRealm().copyToRealmOrUpdate(despesa);

                                    activity.getRealm().commitTransaction();

                                    activity.setTodasAsReceitas(activity.getRealm().where(Receita.class).findAll());
                                    activity.setTodasAsDespesas(activity.getRealm().where(Despesa.class).findAll());

                                    cleanValue = null;

                                    activity.prepareListData();

                                } else {
                                    Receita receita = new Receita();
                                    receita.setId(activity.getUniqueId());
                                    if (!activity.getRealm().isInTransaction()) {
                                        activity.getRealm().beginTransaction();
                                    }
                                    receita.setCategoria(migrationObject.getCategoria());

                                    receita.setDescricao(sa_edt_form_description.getText().toString());
                                    receita.setValor(Double.valueOf(cleanValue));

                                    receita.setDateRegOrUpd(new Date());

                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                    Date date = new Date();

                                    if (sa_edt_form_date.getText() == null) {
                                        sa_edt_form_date.setText(String.valueOf(sdf.format(date)));
                                    } else {
                                        if (sa_edt_form_date.getText().toString().equals("")) {
                                            sa_edt_form_date.setText(String.valueOf(sdf.format(date)));
                                        }
                                    }

                                    receita.setDataVenc(sa_edt_form_date.getText().toString());

                                    activity.getRealm().copyToRealmOrUpdate(receita);

                                    activity.getRealm().commitTransaction();

                                    activity.setTodasAsReceitas(activity.getRealm().where(Receita.class).findAll());
                                    activity.setTodasAsDespesas(activity.getRealm().where(Despesa.class).findAll());

                                    activity.prepareListData();

                                    cleanValue = null;
                                }

                                sDialog.dismiss();
                            } else {
//
//
                            }

                        } else {
                            Toast.makeText(_context, "Por favor, preencha um valor para esta " + migrationObject.getCategoria(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        if (!sweetAlertDialog.isShowing()) {
            sweetAlertDialog.show();

            sweetAlertDialog.changeFormVisibility();

            sa_edt_form_category = sweetAlertDialog.getEdt_form_category();
            sa_edt_form_description = sweetAlertDialog.getEdt_form_description();
            sa_edt_form_value = sweetAlertDialog.getEdt_form_value();
            sa_edt_form_date = sweetAlertDialog.getEdt_form_date();

            sa_edt_form_value.addTextChangedListener(formatValue());

            sa_edt_form_value.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            sa_edt_form_value.setTransformationMethod(new NumericKeyBoardTransformationMethod());

            sa_edt_form_category.setCompoundDrawablesWithIntrinsicBounds(Integer.valueOf(configureIcon(migrationObject.getCategoria())), 0, 0, 0);

            sa_edt_form_category.setText(migrationObject.getCategoria());
//            sa_edt_form_description.setText(migrationObject.getDescricao());
//            sa_edt_form_value.setText(String.valueOf(migrationObject.getValor() / 100));
//            sa_edt_form_date.setText(migrationObject.getDataVenc());

            if (migrationObject.getTipo().equals("Receita")) {
                sa_edt_form_date.setVisibility(View.GONE);
            }

            rl_form = sweetAlertDialog.getRl_form();
        }
    }

    private class NumericKeyBoardTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return source;
        }
    }

}