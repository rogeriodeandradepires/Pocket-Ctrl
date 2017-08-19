package design.appstudio.BudgetCtrl;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.wangjie.wheelview.WheelView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import cn.pedant.SweetAlert.SweetAlertDialog;
import design.appstudio.BudgetCtrl.customfonts.daxpro.DaxProLittleBoldTextView;
import design.appstudio.BudgetCtrl.realm.model.Despesa;
import design.appstudio.BudgetCtrl.realm.model.MigrationObject;
import design.appstudio.BudgetCtrl.realm.model.Receita;
import design.appstudio.BudgetCtrl.simplelistview.adapter.SimpleListAdapter;
import design.appstudio.BudgetCtrl.simplelistview.recyclermodels.SubjectsRecycler;
import io.realm.Realm;
import io.realm.RealmConfiguration;

import static design.appstudio.BudgetCtrl.R.id.rl_balance_background;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private SimpleListAdapter listAdapter;
    private ListView listView;
    private List<SubjectsRecycler> subjectsRecyclers;

    private List<List<SubjectsRecycler>> listaDaLista = new ArrayList<>();

    private SharedPreferences isGrouped;
    private boolean isGroupedBoolean = false;
    private boolean toastBoolean = false;
    private Realm realm;

    private double totalOrcamento = 0;
    private double totalDespesas = 0;
    private double saldo = 0;

    private ViewGroup holderParent;

    private List<Receita> todasAsReceitas;
    private List<Despesa> todasAsDespesas;

    private DaxProLittleBoldTextView nada_cadastrado_geral;//, //nada_cadastrado_orcamento, //nada_cadastrado_despesas;

    private FrameLayout btnAddBudget, btnDeleteBudgets, btnAddExpense, btnDeleteExpenses, btnStartOver;
    //    private ImageView btnHideBudget;
    private SweetAlertDialog sweetAlertDialog;
    private List<String> listBudgetTypes = new ArrayList<>();
    private List<String> listExpenseTypes = new ArrayList<>();

    private RelativeLayout rl_form;
    private TextInputEditText edt_form_category;
    private TextInputEditText edt_form_description;
    private TextInputEditText edt_form_value;
    private TextInputEditText edt_form_date;

    private EditText edt_budget_value, edt_expenses_value, edt_balance_value, edt_balance_money_symbol, edt_despesas_symbol;
    private RelativeLayout rl_balance_bg, rl_despesas_bg;
    private DaxProLittleBoldTextView balance_title;
    private ImageView saldo_icon;

    private View incl_balance_layout, incl_despesas_layout, incl_orcamento_layout;

    private DatePickerDialog datePickerDialog;

    private WheelView wv;

    private String cleanValue;

    private static final Set<Long> usedIds = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

//        Realm.init(this);

        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
        // Use the config
        realm = Realm.getInstance(config);

        isGrouped = getSharedPreferences("items.grouped", Context.MODE_PRIVATE);
        isGroupedBoolean = isGrouped.getBoolean("isGrouped", false);

        Toolbar appBar = (Toolbar) findViewById(R.id.toolbar);

        nada_cadastrado_geral = (DaxProLittleBoldTextView) findViewById(R.id.tv_nada_cadastrado_geral);
//        //nada_cadastrado_orcamento = (DaxProLittleBoldTextView) findViewById(R.id.tv_//nada_cadastrado_orcamento);
//        //nada_cadastrado_despesas = (DaxProLittleBoldTextView) findViewById(R.id.tv_//nada_cadastrado_despesas);

        btnAddBudget = (FrameLayout) findViewById(R.id.btn_add_budget);
        btnDeleteBudgets = (FrameLayout) findViewById(R.id.btn_delete_budget);
        btnAddExpense = (FrameLayout) findViewById(R.id.btn_add_despesa);
        btnDeleteExpenses = (FrameLayout) findViewById(R.id.btn_delete_despesa);
        btnStartOver = (FrameLayout) findViewById(R.id.btn_start_over);
//        btnHideBudget = (ImageView) findViewById(R.id.budget_icon_hide);

        incl_orcamento_layout = findViewById(R.id.incl_app_bar);
        incl_despesas_layout = findViewById(R.id.incl_app_bar_footer);
        incl_balance_layout = findViewById(R.id.incl_saldo_footer);

        edt_budget_value = (EditText) incl_orcamento_layout.findViewById(R.id.edt_budget_value);
        edt_expenses_value = (EditText) incl_despesas_layout.findViewById(R.id.edt_expenses_value);

        edt_balance_value = (EditText) incl_balance_layout.findViewById(R.id.edt_balance_value);
        edt_balance_money_symbol = (EditText) incl_balance_layout.findViewById(R.id.edt_balance_money_symbol);
        rl_balance_bg = (RelativeLayout) incl_balance_layout.findViewById(rl_balance_background);

        rl_despesas_bg = (RelativeLayout) incl_despesas_layout.findViewById(R.id.rl_despesas_bg);
        edt_despesas_symbol = (EditText) incl_despesas_layout.findViewById(R.id.edt_despesas_symbol);

        balance_title = (DaxProLittleBoldTextView) incl_balance_layout.findViewById(R.id.lbl_balance);
        saldo_icon = (ImageView) incl_balance_layout.findViewById(R.id.saldo_icon);

//        btnHideBudget.setTag(R.drawable.ic_svg_hide);

        setSupportActionBar(appBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appBar.setElevation(0);
            appBar.setOutlineProvider(null);
        }

        // get the listview
        listView = (ListView) findViewById(R.id.lv_budgets);

//        listView.setDivider(getResources().getDrawable(R.color.colorTransparent));
//        listView.setDivider(getResources().getDrawable(R.color.colorTransparent));
//        listView.setDividerHeight(0);

        // preparing list data
        prepareListData();

        configureButtons();

        prepareCategories();

//        listView.setSelection(listAdapter.getCount() - 1);

    }

    private void prepareCategories() {
        listBudgetTypes.add("Cartão");
        listBudgetTypes.add("Empréstimo");
        listBudgetTypes.add("Poupança");
        listBudgetTypes.add("Salário");
        listBudgetTypes.add("Outros");

        listExpenseTypes.add("Água");
        listExpenseTypes.add("Cartão");
        listExpenseTypes.add("Casa");
        listExpenseTypes.add("Combustível");
        listExpenseTypes.add("Contas");
        listExpenseTypes.add("Cosméticos");
        listExpenseTypes.add("Dependentes");
        listExpenseTypes.add("Diversão");
        listExpenseTypes.add("Educação");
        listExpenseTypes.add("Eletrônicos");
        listExpenseTypes.add("Empréstimo");
        listExpenseTypes.add("Energia");
        listExpenseTypes.add("Esportes");
        listExpenseTypes.add("Mercado");
        listExpenseTypes.add("Pets");
        listExpenseTypes.add("Poupança");
        listExpenseTypes.add("Presente");
        listExpenseTypes.add("Saúde");
        listExpenseTypes.add("Serviços");
        listExpenseTypes.add("Transporte");
        listExpenseTypes.add("Vestuário");
        listExpenseTypes.add("Viagem");
        listExpenseTypes.add("Outros");

    }

    private void configureButtons() {

//        btnHideBudget.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (Integer.valueOf(btnHideBudget.getTag().toString()) == R.drawable.ic_svg_hide) {
//                    btnHideBudget.setImageDrawable(getResources().getDrawable(R.drawable.ic_svg_show));
//                    btnHideBudget.setTag(R.drawable.ic_svg_show);
//                } else {
//                    btnHideBudget.setImageDrawable(getResources().getDrawable(R.drawable.ic_svg_hide));
//                    btnHideBudget.setTag(R.drawable.ic_svg_hide);
//                }
//            }
//        });

        btnAddBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnAddBudget.isActivated()) {
                    btnAddBudget.setBackgroundResource(R.drawable.bg_btn_add_budget_selector_clicked);
                }

                newRegister("Receita", listBudgetTypes);

            }
        });

        btnDeleteBudgets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnDeleteBudgets.isActivated()) {
                    btnDeleteBudgets.setBackgroundResource(R.drawable.bg_btn_delete_budget_selector_clicked);
                }

                if (todasAsReceitas.size() > 0) {
                    removeAll("Receita");
                } else {
                    Toast.makeText(MainActivity.this, "Nenhuma Receita Registrada", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnAddExpense.isActivated()) {
                    btnAddExpense.setBackgroundResource(R.drawable.bg_btn_add_budget_selector_clicked);
                }

                newRegister("Despesa", listExpenseTypes);

            }
        });

        btnDeleteExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnDeleteExpenses.isActivated()) {
                    btnDeleteExpenses.setBackgroundResource(R.drawable.bg_btn_delete_budget_selector_clicked);
                }

                if (todasAsDespesas.size() > 0) {
                    removeAll("Despesa");
                } else {
                    Toast.makeText(MainActivity.this, "Nenhuma Despesa Registrada", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnStartOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "";
                int receitasSize = todasAsReceitas.size();
                int despesasSize = todasAsDespesas.size();
                if (btnStartOver.isActivated()) {
                    btnStartOver.setBackgroundResource(R.drawable.bg_btn_delete_budget_selector_clicked);
                }

                if (receitasSize > 0 && despesasSize > 0) {
                    message = "Todos os registros foram excluídos com sucesso!";

                } else {
                    if (receitasSize > 0) {
                        message = "Não havia Despesas registradas, mas todas as Receitas foram excluídas com sucesso!";
                    }

                    if (despesasSize > 0) {
                        message = "Não havia Receitas registradas, mas todas as Despesas foram excluídas com sucesso!";
                    }
                }

                if (!message.equals("")) {
                    startOver(message);
                } else {
                    Toast.makeText(MainActivity.this, "Nenhum Registro Encontrado", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected
        getMenuInflater().inflate(R.menu.main, menu);

        if (isGroupedBoolean) {
            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_svg_ungroup));
        } else {
            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_svg_group));
        }

//        menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_svg_menu));
//        menu.


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            mensagem_fechar();

            return true;
        }

        if (id == R.id.action_group_ungroup) {

            if (isGroupedBoolean) {
                item.setIcon(getResources().getDrawable(R.drawable.ic_svg_group));
            } else {
                item.setIcon(getResources().getDrawable(R.drawable.ic_svg_ungroup));
            }

            isGrouped.edit().putBoolean("isGrouped", !isGroupedBoolean).apply();
            isGroupedBoolean = !isGroupedBoolean;
            toastBoolean = !toastBoolean;
//            listAdapter.notifyDataSetChanged();

            prepareListData();

        }

        return super.onOptionsItemSelected(item);
    }

    public void calculaValores() {
        totalDespesas = 0;
        totalOrcamento = 0;

        for (Despesa despesa : todasAsDespesas) {
            totalDespesas += despesa.getValor();
        }

        for (Receita receita : todasAsReceitas) {
            totalOrcamento += receita.getValor();
        }

//        Toast.makeText(this, "saldo="+saldo+", orcamento="+totalOrcamento+", despesas="+totalDespesas, Toast.LENGTH_SHORT).show();

        saldo = totalOrcamento - totalDespesas;

        String totalDespesasFormatted = NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format((totalDespesas / 100));
        String totalOrcamentoFormatted = NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format((totalOrcamento / 100));
        String saldoFormatted = NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format((saldo / 100));

        edt_expenses_value.setText(totalDespesasFormatted.replace("R$", ""));
        edt_budget_value.setText(totalOrcamentoFormatted.replace("R$", ""));

        if (saldo < 0) {
            edt_balance_value.setText(saldoFormatted.replace("R$", " "));
            edt_balance_value.setTextColor(getResources().getColor(R.color.darkRedForBalance));
            edt_balance_money_symbol.setText(" R$ ");
            edt_balance_money_symbol.setTextColor(getResources().getColor(R.color.darkRedForBalance));
            edt_balance_money_symbol.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_svg_chart_one_red, 0, 0, 0);

            rl_balance_bg.setBackground(getResources().getDrawable(R.drawable.bg_balance_red_gradienttoolbar));
            balance_title.setTextColor(getResources().getColor(R.color.darkRed));
            balance_title.setShadowLayer(2f, 3, 3, getResources().getColor(R.color.darkRedForBalance));
            saldo_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_svg_chart_two_red));

            rl_despesas_bg.setBackgroundColor(getResources().getColor(R.color.lightRedForBalance));
            edt_despesas_symbol.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_svg_expenses_red, 0, 0, 0);

        } else {
            edt_balance_value.setText(saldoFormatted.replace("R$", ""));
            edt_balance_value.setTextColor(getResources().getColor(R.color.color2Gradient));
            edt_balance_money_symbol.setText(" R$ ");
            edt_balance_money_symbol.setTextColor(getResources().getColor(R.color.color2Gradient));
            edt_balance_money_symbol.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_svg_chart_one, 0, 0, 0);

            rl_balance_bg.setBackground(getResources().getDrawable(R.drawable.bg_balance_green_gradienttoolbar));
            balance_title.setTextColor(getResources().getColor(R.color.color2GradientForBalance));
            balance_title.setShadowLayer(2f, 3, 3, getResources().getColor(R.color.color2Gradient));
            saldo_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_svg_chart_two));

            rl_despesas_bg.setBackgroundColor(getResources().getColor(R.color.color1Gradient));
            edt_despesas_symbol.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_svg_expenses, 0, 0, 0);
        }

    }

    public void prepareListData() {

        subjectsRecyclers = new ArrayList<>();

        if (holderParent != null) {
            holderParent.removeAllViewsInLayout();
        }

        todasAsReceitas = realm.where(Receita.class).findAll();
        todasAsDespesas = realm.where(Despesa.class).findAll();

        List<List<Despesa>> despesasIncluidas = new ArrayList<>();
        listAdapter = new SimpleListAdapter(MainActivity.this, subjectsRecyclers, MainActivity.this);

        List<MigrationObject> migrationObjects = new ArrayList<>();

        // setting list adapter
        listView.setAdapter(listAdapter);

        //adiciona Orçamentos como primeiro item da lista, para ficar em o title background


        for (Receita receita : todasAsReceitas) {
            migrationObjects.add(receita.migrate());
//            addToListView(receita.migrate());//,subjectsRecyclers.size(),subjectsRecyclers.size());
        }

        for (Despesa despesa : todasAsDespesas) {
            migrationObjects.add(despesa.migrate());
//            addToListView(despesa.migrate());//,subjectsRecyclers.size(),subjectsRecyclers.size());
        }

        listView.setDivider(getResources().getDrawable(R.color.colorTransparent));
        listView.setDividerHeight(0);

        if (isGroupedBoolean) {

            if (!toastBoolean) {
                Toast.makeText(this, "Ordenado por Categoria e Data", Toast.LENGTH_SHORT).show();
                toastBoolean = !toastBoolean;
            }

            Collections.sort(migrationObjects, new Comparator<MigrationObject>() {
                @Override
                public int compare(MigrationObject o1, MigrationObject o2) {

                    int retorno = 0;
                    int comparacao = o1.getTipo().compareTo(o2.getTipo());
                    if (comparacao == 0) {
                        int comparacao2 = (o1.getCategoria().replace("Á", "A")).compareTo(o2.getCategoria().replace("Á", "A"));
                        if (comparacao2 == 0) {
                            retorno = o1.getDateRegOrUpd().compareTo(o2.getDateRegOrUpd());
                        } else {
                            retorno = comparacao2;
                        }
                    }
                    return retorno;
                }
            });
        } else {

//            listView.setDivider(getResources().getDrawable(R.color.colorTransparent));
//            listView.setDividerHeight(0);

            if (!toastBoolean) {
                Toast.makeText(this, "Ordenado por Data", Toast.LENGTH_SHORT).show();
                toastBoolean = !toastBoolean;
            }
            Collections.sort(migrationObjects, new Comparator<MigrationObject>() {
                @Override
                public int compare(MigrationObject o1, MigrationObject o2) {
                    int data = o1.getDateRegOrUpd().compareTo(o2.getDateRegOrUpd());
                    if (data != 0) {
                        return data;
                    } else {
                        return 0;
                    }
                }
            });

        }

        for (MigrationObject migrationObject : migrationObjects) {
            if (migrationObjects.indexOf(migrationObject) == 0) {
                addToListView(migrationObject, true);
            } else {
                if (!migrationObject.getCategoria().equals(migrationObjects.get((migrationObjects.indexOf(migrationObject) - 1)).getCategoria())) {
                    addToListView(migrationObject, true);
                } else {
                    addToListView(migrationObject, false);
                }
            }
        }

        if (subjectsRecyclers.size() > 3) {

//            //nada_cadastrado_despesas.setVisibility(View.GONE);
            nada_cadastrado_geral.setVisibility(View.GONE);
//            //nada_cadastrado_orcamento.setVisibility(View.GONE);

        } else {
            if (todasAsReceitas.size() == 0) {
                if (todasAsDespesas.size() == 0) {

                    //nada_cadastrado_despesas.setVisibility(View.GONE);
                    nada_cadastrado_geral.setVisibility(View.VISIBLE);
                    //nada_cadastrado_orcamento.setVisibility(View.GONE);

                    listView.setVisibility(View.GONE);
                } else {

                    //nada_cadastrado_despesas.setVisibility(View.GONE);
                    nada_cadastrado_geral.setVisibility(View.GONE);
                    //nada_cadastrado_orcamento.setVisibility(View.VISIBLE);

                    listView.setVisibility(View.VISIBLE);
                }
            } else {
                if (todasAsDespesas.size() == 0) {
                    //nada_cadastrado_despesas.setVisibility(View.VISIBLE);
                    nada_cadastrado_geral.setVisibility(View.GONE);
                    //nada_cadastrado_orcamento.setVisibility(View.GONE);

                } else {
                    //nada_cadastrado_despesas.setVisibility(View.GONE);
                    nada_cadastrado_geral.setVisibility(View.GONE);
                    //nada_cadastrado_orcamento.setVisibility(View.GONE);
                }
                listView.setVisibility(View.VISIBLE);
            }
        }

        calculaValores();

    }

    public void addToListView(MigrationObject migrationObject, boolean isCategoryHeader) {//}, int catPosition, int itemPosition) {

        final SubjectsRecycler subject = new SubjectsRecycler();

        subject.setCategoria(migrationObject.getCategoria());

        subject.setHeaderTitle(migrationObject.getCategoria());

        subject.setMigrationObject(migrationObject);

        if (isCategoryHeader) {
            subject.setCategoryHeader(true);
        }

        subject.setRealm(realm);

        subjectsRecyclers.add(subject);

        listAdapter.notifyDataSetChanged();

        calculaValores();

        listView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                for (int i = 0; i < listAdapter.getCount(); i++) {
                    if (listAdapter.getItem(i).equals(subject)) {
                        listView.setSelection(i);
                    }
                }
//                listView.setSelection(listAdapter.getCount() - 1);
            }
        });

    }

    private void recursiveSorter(List<SubjectsRecycler> sortedSubjectRecyclersByReceita) {

        List<SubjectsRecycler> tempList = new ArrayList<>();
        List<SubjectsRecycler> removedItemsList = new ArrayList<>();

        //por categoria
        for (SubjectsRecycler subjectRecycler : sortedSubjectRecyclersByReceita) {
            if (sortedSubjectRecyclersByReceita.indexOf(subjectRecycler) != 0) {
                if (subjectRecycler.getCategoria()
                        .equals(sortedSubjectRecyclersByReceita.get((sortedSubjectRecyclersByReceita.indexOf(subjectRecycler) - 1)).getCategoria())) {
                    tempList.add(subjectRecycler);
                }
            } else {
                tempList.add(subjectRecycler);
            }
        }

        if (tempList.size() != 0) {
            System.out.println("16/08/2017: sort tempList.size " + tempList.size());
            System.out.println("16/08/2017: sort Não Zero ANTES listaDaLista.size " + listaDaLista.size());
            listaDaLista.add(tempList);
            listaDaLista = new ArrayList<>(new LinkedHashSet<List<SubjectsRecycler>>(listaDaLista));
            System.out.println("16/08/2017: sort Não Zero DEPOIS listaDaLista.size " + listaDaLista.size());
            removedItemsList.addAll(sortedSubjectRecyclersByReceita);
            removedItemsList.removeAll(tempList);
//            removedItemsList = sortedSubjectRecyclersByReceita;
            recursiveSorter(removedItemsList);
        } else {

        }

    }

    public void mensagem_fechar() {

//        sweetAlertDialog.getWheel().setVisibility(View.GONE);

        sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText("Fechar")
                .cleanWheelData()
                .setContentText("Tem certeza que quer fechar o aplicativo?")
                .setCancelText("Não")
                .setConfirmText("Sim")
                .showCancelButton(true)
                .setCustomImage(R.drawable.ic_svg_sweetalert_question)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        finish();
                        System.exit(0);
                    }
                });

        if (!sweetAlertDialog.isShowing()) {
            sweetAlertDialog.show();
        }
    }

    public int configureIcon(String category) {
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

    public void newRegister(final String type, List<String> categories) {

        final WheelView finalWv = wv;
        sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setWheelData(categories)
                .setTitleText("Nova " + type)
                .setContentText("Selecione a categoria da " + type)
                .setCancelText("Cancelar")
                .setConfirmText("Ok")
                .showCancelButton(true)
                .setCustomImage(type.equals("Despesa") ? R.drawable.ic_svg_expense_red : R.drawable.ic_svg_revenue_green)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        edt_form_category.setText(wv.getSeletedItem());
                        edt_form_category.setCompoundDrawablesWithIntrinsicBounds(Integer.valueOf(configureIcon(wv.getSeletedItem())), 0, 0, 0);

                        long date = System.currentTimeMillis();

                        SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
                        int dateStringYear = Integer.valueOf(sdfYear.format(date));
                        SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
                        int dateStringMonth = Integer.valueOf(sdfMonth.format(new Date())) - 1;
                        SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
                        int dateStringDay = Integer.valueOf(sdfDay.format(date));

                        datePickerDialog = new DatePickerDialog(MainActivity.this, MainActivity.this, dateStringYear, dateStringMonth, dateStringDay);
                        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());

                        edt_form_date.addTextChangedListener(formatVencimento());
                        edt_form_value.addTextChangedListener(formatValue());

                        edt_form_value.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                        edt_form_value.setTransformationMethod(new NumericKeyBoardTransformationMethod());

                        configureEdtFocus();

                        sDialog.cleanWheelData()
                                .showContentText(false)
                                .setContentText("")
                                .setCancelText("Cancelar")
                                .setConfirmText("Salvar")
                                .showCancelButton(true)
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {

//                                        Toast.makeText(MainActivity.this, "CleanValue=" + cleanValue, Toast.LENGTH_SHORT).show();

                                        if (cleanValue != null) {

                                            if (Double.valueOf(cleanValue) > 0) {
                                                if (type.equals("Despesa")) {
                                                    Despesa despesa = new Despesa();
//                                                    usedIds = realm.where(Despesa.class).findAll()
                                                    despesa.setId(getUniqueId());
                                                    despesa.setCategoria(edt_form_category.getText().toString());

                                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                                    Date date = new Date();
//                                                    date = sdf.format(date);

                                                    if (edt_form_date.getText() == null) {

                                                        edt_form_date.setText(String.valueOf(sdf.format(date)));
                                                    } else {
                                                        if (edt_form_date.getText().toString().equals("")) {
                                                            edt_form_date.setText(String.valueOf(sdf.format(date)));
                                                        }
                                                    }

                                                    despesa.setDataVenc(edt_form_date.getText().toString());

                                                    despesa.setDescricao(edt_form_description.getText().toString());
                                                    despesa.setValor(Double.valueOf(cleanValue));

                                                    despesa.setDateRegOrUpd(new Date());

                                                    if (!realm.isInTransaction()) {
                                                        realm.beginTransaction();
                                                    }

                                                    realm.copyToRealmOrUpdate(despesa);

                                                    realm.commitTransaction();

                                                    cleanValue = null;

//                                                    prepareListData();
                                                    todasAsReceitas = realm.where(Receita.class).findAll();
                                                    todasAsDespesas = realm.where(Despesa.class).findAll();

//                                                    holderParent.removeAllViewsInLayout();

//                                                    addToListView(despesa.migrate());
                                                    prepareListData();


                                                } else {
                                                    Receita receita = new Receita();
//                                                    usedIds = realm.where(Despesa.class).findAll()
                                                    receita.setId(getUniqueId());
                                                    receita.setCategoria(edt_form_category.getText().toString());

//                                                    if (edt_form_date.getText() == null || edt_form_date.getText().equals("")) {
//                                                        edt_form_date.setText((new Date()).toString());
//                                                        Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
//                                                    }
//
//                                                    receita.setDataVenc(edt_form_date.getText().toString());

                                                    receita.setDescricao(edt_form_description.getText().toString());
                                                    receita.setValor(Double.valueOf(cleanValue));

                                                    receita.setDateRegOrUpd(new Date());

                                                    if (!realm.isInTransaction()) {
                                                        realm.beginTransaction();
                                                    }

                                                    realm.copyToRealmOrUpdate(receita);

                                                    realm.commitTransaction();

                                                    cleanValue = null;

                                                    todasAsReceitas = realm.where(Receita.class).findAll();
                                                    todasAsDespesas = realm.where(Despesa.class).findAll();

//                                                    holderParent.removeAllViewsInLayout();

//                                                    prepareListData();
//                                                    addToListView(receita.migrate());
                                                    prepareListData();
                                                }

                                                sDialog.dismiss();
                                            } else {


                                            }

                                        } else {
                                            Toast.makeText(MainActivity.this, "Por favor, preencha um valor para esta " + type, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                        sDialog.changeFormVisibility();
                    }
                });

        if (!sweetAlertDialog.isShowing()) {
            sweetAlertDialog.show();

            wv = sweetAlertDialog.getWheel();

            edt_form_category = sweetAlertDialog.getEdt_form_category();
            edt_form_description = sweetAlertDialog.getEdt_form_description();
            edt_form_value = sweetAlertDialog.getEdt_form_value();
            edt_form_date = sweetAlertDialog.getEdt_form_date();

            if (type.equals("Receita")) {
                edt_form_date.setVisibility(View.GONE);
            }

            rl_form = sweetAlertDialog.getRl_form();

        }
    }

    private void configureEdtFocus() {

        edt_form_value.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
//                    rl_form.requestFocus();
                }
            }
        });

        edt_form_description.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
//                    rl_form.requestFocus();
                }
            }
        });

        edt_form_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
//                    rl_form.requestFocus();
                    hideSoftKeyboard(MainActivity.this);
                    datePickerDialog.show();

                    datePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            rl_form.requestFocus();
//                            edtDayOfEvent.setText();
                        }
                    });

                }
            }
        });

    }

    public void startOver(final String message) {
        sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Recomeçar")
                .cleanWheelData()
                .setContentText("Tem certeza que quer excluir todos os registros para recomeçar?")
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
                        if (!realm.isInTransaction()) {
                            realm.beginTransaction();
                        }

                        realm.where(Despesa.class).findAll().deleteAllFromRealm();
                        realm.where(Receita.class).findAll().deleteAllFromRealm();

                        realm.commitTransaction();
                        prepareListData();

                        successMessage("Excluído", message, sDialog);

                        //15/08/2017
//                        notifyDataSet
                    }
                });

        if (!sweetAlertDialog.isShowing()) {
            sweetAlertDialog.show();
        }

    }

    public void successMessage(String title, String message, SweetAlertDialog sDialog) {
        sDialog.setTitleText(title)
                .setContentText(message)
                .showCancelButton(false)
                .setConfirmText("Ok")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                    }
                });

        sDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

    }

    public void removeAll(final String type) {
        sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Excluir " + type + "s")
                .cleanWheelData()
                .setContentText("Tem certeza que quer excluir todas as " + type + "s?")
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
                        if (!realm.isInTransaction()) {
                            realm.beginTransaction();
                        }

                        realm.where((type.equals("Despesa") ? Despesa.class : Receita.class)).findAll().deleteAllFromRealm();

                        realm.commitTransaction();
//                        prepareListData();

                        successMessage("Excluído", "Todas as " + type + "s foram excluídas com sucesso!", sDialog);

                        List<SubjectsRecycler> listSubjectsForRemoving = new ArrayList<>();

                        for (SubjectsRecycler subjectsRecycler : subjectsRecyclers) {
                            if (subjectsRecycler.getMigrationObject().getTipo().equals(type)) {
                                listSubjectsForRemoving.add(subjectsRecycler);
                            }
                        }

                        subjectsRecyclers.removeAll(listSubjectsForRemoving);

                        listAdapter.notifyDataSetChanged();
                        calculaValores();
//                        subjectsRecyclers.get()


                        //15/08/2017
//                        notifyDataSet
                    }
                });

        if (!sweetAlertDialog.isShowing()) {
            sweetAlertDialog.show();
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

    public Realm getRealm() {
        return realm;
    }

    public void setRealm(Realm realm) {
        this.realm = realm;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month = month + 1;
        String dataString = String.valueOf(dayOfMonth > 9 ? dayOfMonth : "0" + dayOfMonth);
        dataString += (month > 9 ? month : "0" + month);
        dataString += year;

        if (edt_form_date != null) {
            edt_form_date.setText(dataString);
        }


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
                    edt_form_date.setText(current);
                    edt_form_date.setSelection(sel < current.length() ? sel : current.length());

//
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }


        };

        return retorno;

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
                    edt_form_value.removeTextChangedListener(this);

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
                    edt_form_value.setText(formatted);
                    edt_form_value.setSelection(formatted.length());

                    edt_form_value.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }


        };

        return retorno;

    }

    public void removeItem(final MigrationObject migrationObject, final ViewGroup holderParent, final View holderView) {
        sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
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
                        if (!realm.isInTransaction()) {
                            realm.beginTransaction();
                        }

                        realm.where((migrationObject.getTipo().equals("Despesa") ? Despesa.class : Receita.class)).equalTo("id", migrationObject.getId()).findFirst().deleteFromRealm();

                        realm.commitTransaction();
//                        prepareListData();

                        successMessage("Excluído", " Esta " + migrationObject.getTipo() + " foi excluída com sucesso!", sDialog);

                        List<SubjectsRecycler> listSubjectsForRemoving = new ArrayList<>();

                        for (SubjectsRecycler subjectsRecycler : subjectsRecyclers) {
                            if (subjectsRecycler.getMigrationObject().getId() == migrationObject.getId()) {
                                listSubjectsForRemoving.add(subjectsRecycler);
                            }
                        }

                        subjectsRecyclers.removeAll(listSubjectsForRemoving);

                        setTodasAsReceitas(realm.where(Receita.class).findAll());
                        setTodasAsDespesas(realm.where(Despesa.class).findAll());

                        holderParent.removeViewInLayout(holderView);

                        getListAdapter().notifyDataSetChanged();
                        calculaValores();
//                        subjectsRecyclers.get()


                        //15/08/2017
//                        notifyDataSet
                    }
                });

        if (!sweetAlertDialog.isShowing()) {
            sweetAlertDialog.show();
        }
    }

    public static synchronized long getUniqueId() {
        long millis;
        do {
            millis = System.currentTimeMillis();
        } while (!usedIds.add(millis));
        return millis;
    }

    public String categorySum(String category, String type) {

        String retorno = "";

        double categoryExpenses = 0;
        double categoryBudgets = 0;

        for (Despesa despesa : todasAsDespesas) {

            if (despesa.migrate().getCategoria().equals(category)) {
                categoryExpenses += despesa.getValor();
            }
        }

        for (Receita receita : todasAsReceitas) {
            if (receita.migrate().getCategoria().equals(category)) {
                categoryBudgets += receita.getValor();
            }
        }

        String totalDespesasCategoryFormatted = NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format((categoryExpenses / 100));
        String totalOrcamentoCategoryFormatted = NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format((categoryBudgets / 100));

        retorno = type.equals("Despesa") ? totalDespesasCategoryFormatted.replace("R$", "") : totalOrcamentoCategoryFormatted.replace("R$", "");

//        Toast.makeText(this, "retorno="+retorno, Toast.LENGTH_SHORT).show();

        return retorno;
    }

    public String getCleanValue() {
        return cleanValue;
    }

    public void setCleanValue(String cleanValue) {
        this.cleanValue = cleanValue;
    }

    public List<Receita> getTodasAsReceitas() {
        return todasAsReceitas;
    }

    public void setTodasAsReceitas(List<Receita> todasAsReceitas) {
        this.todasAsReceitas = todasAsReceitas;
    }

    public List<Despesa> getTodasAsDespesas() {
        return todasAsDespesas;
    }

    public void setTodasAsDespesas(List<Despesa> todasAsDespesas) {
        this.todasAsDespesas = todasAsDespesas;
    }

    public SimpleListAdapter getListAdapter() {
        return listAdapter;
    }

    public void setListAdapter(SimpleListAdapter listAdapter) {
        this.listAdapter = listAdapter;
    }

    public ViewGroup getHolderParent() {
        return holderParent;
    }

    public void setHolderParent(ViewGroup holderParent) {
        this.holderParent = holderParent;
    }

    public boolean isGroupedBoolean() {
        return isGroupedBoolean;
    }

    public void setGroupedBoolean(boolean groupedBoolean) {
        isGroupedBoolean = groupedBoolean;
    }

    public void removeAllFromCategory(final String category, final String tipo) {
        sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Excluir " + tipo + "s")
                .cleanWheelData()
                .setContentText("Tem certeza que quer excluir todas as " + tipo + "s da Categoria " + category + "?")
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
                        if (!realm.isInTransaction()) {
                            realm.beginTransaction();
                        }

                        realm.where((tipo.equals("Despesa") ? Despesa.class : Receita.class)).equalTo("categoria", category).findAll().deleteAllFromRealm();

                        realm.commitTransaction();

                        successMessage("Excluído", "Todas as " + tipo + "s da Categoria " + category + " foram excluídas com sucesso!", sDialog);
// muda a posição da lista, forçando um scroll bottom
                        prepareListData();

//                        List<SubjectsRecycler> listSubjectsForRemoving = new ArrayList<>();
//
//                        for (SubjectsRecycler subjectsRecycler : subjectsRecyclers) {
//                            if (subjectsRecycler.getMigrationObject().getTipo().equals(type)) {
//                                listSubjectsForRemoving.add(subjectsRecycler);
//                            }
//                        }
//
//                        subjectsRecyclers.removeAll(listSubjectsForRemoving);
//
//                        holderParent.removeAllViewsInLayout();
//
//                        listAdapter.notifyDataSetChanged();
//                        calculaValores();
//                        subjectsRecyclers.get()
//
//
//                        //15/08/2017
////                        notifyDataSet
                    }
                });

        if (!sweetAlertDialog.isShowing()) {
            sweetAlertDialog.show();
        }
    }

    public ListView getListView() {
        return listView;
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }

    private class NumericKeyBoardTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return source;
        }
    }
}
