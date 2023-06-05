package com.app.fitlife

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.appcompat.widget.SwitchCompat
import androidx.cardview.widget.CardView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.app.fitlife.data.AppDataBase
import com.app.fitlife.data.User
import com.app.fitlife.data.UserDao
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class SignUpActivity : AppCompatActivity(), LifecycleOwner {
    private lateinit var dataBase: AppDataBase

    private lateinit var spinnerWeek: Spinner
    private lateinit var spinnerExercise: Spinner
    private lateinit var dataTextView: TextView
    private lateinit var dataButton: CardView
    private lateinit var saveBtn: Button
    private lateinit var dao: UserDao
    private var selectedDate: String? = null
    var SpinnerWeekString: String = ""
    var SpinnerTypeString: String = ""
    private var userData: User? = null

    private lateinit var passwordWarning:TextView
    private lateinit var edtTextName:TextView
    private lateinit var edtTextEmail:TextView
    private lateinit var edtTextPassword:TextView
    private lateinit var edtTextWeight:TextView
    private lateinit var edtHeight:TextView

    private lateinit var gender:String
    private lateinit var goal:String


    private lateinit var genderRadioGroup:RadioGroup
    private lateinit var goalsRadioGroup:RadioGroup
    private lateinit var radioButtonFemale:RadioButton
    private lateinit var radioButtonGain:RadioButton
    private lateinit var radioButtonKeep:RadioButton


    private var source: Int = -1

    private lateinit var resultIntent:Intent


    companion object{
        const val SOURCE_LOGIN = 1
        const val SOURCE_MAIN = 2
        const val REGISTER_REQUEST_CODE = 0
        const val RESULT_REGISTER_SUCCESSFUL_LOGIN = 1001
        const val RESULT_REGISTER_SUCCESSFUL_MAIN = 1002
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // recuperação dos dados no menu editar perfil
        passwordWarning = findViewById<TextView>(R.id.warning_tv)
        edtTextName = findViewById<EditText>(R.id.name_edt_text)
        edtTextEmail = findViewById<EditText>(R.id.email_edt_text)
        edtTextPassword = findViewById<EditText>(R.id.edt_text_password)
        edtTextWeight = findViewById<EditText>(R.id.weight_edt_text)
        edtHeight = findViewById<EditText>(R.id.height_edt_text)
        dataTextView = findViewById(R.id.time_tv)



        //CheckBox Goals
         radioButtonGain = findViewById<RadioButton>(R.id.rb_gain)
        val radioButtonLose = findViewById<RadioButton>(R.id.rb_lose)
        goalsRadioGroup = findViewById<RadioGroup>(R.id.rg_goal)
        radioButtonKeep = findViewById<RadioButton>(R.id.rb_keep)


        //CheckBox Gender
        genderRadioGroup = findViewById<RadioGroup>(R.id.gender_radio_group)
        radioButtonFemale = findViewById<RadioButton>(R.id.rb_female)
        val radioButtonMale = findViewById<RadioButton>(R.id.rb_male)
        gender = if (radioButtonFemale.isSelected) "Feminino" else "Masculino"
        goal = if (radioButtonKeep.isSelected) "Manter" else if (radioButtonGain.isSelected) "Ganhar" else "Emagrecer"

        //Spinner
        spinnerWeek = findViewById<Spinner>(R.id.spinner_weekly)
        spinnerExercise = findViewById<Spinner>(R.id.spinner_exercise_type)


        userData = intent.getSerializableExtra("EXTRA_USER_DATA") as User?

        if (userData != null) {
            edtTextName.text = userData?.name
            edtTextEmail.text = userData?.email
            edtTextPassword.text = userData?.password
            edtTextWeight.text = userData?.weight
            edtHeight.text = userData?.height
            dataTextView.text = userData?.birth
            if (userData?.gender == "Feminino") {
                radioButtonFemale.isChecked = true
            } else {
                radioButtonMale.isChecked = true
            }
            if (userData?.goal == "Manter") {
                radioButtonKeep.isChecked = true
            } else if (userData?.goal == "Ganhar") {
                radioButtonGain.isChecked = true
            } else {
                radioButtonLose.isChecked = true
            }


        }




        //Inicialização do DataBase a partir da classe Application.
        dataBase = (application as FitLifeApplication).getAppDataBase()
        dao = dataBase.userDao()

        //Botão de voltar do Toolbar, caso o usuario deseje sair sem salvar os dados, irá aparecer uma mensagem de confirmação
        val imageView = findViewById<ImageView>(R.id.imageView)
        imageView.setOnClickListener {
            showAlertDialog()
        }


        //Calendario

        dataButton = findViewById(R.id.data_cardview)


        //Guarda a data escolhida pelo usuario
        val calendarBox = Calendar.getInstance()
        val dateBox = DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
            calendarBox.set(Calendar.YEAR, year)
            calendarBox.set(Calendar.MONTH, month)
            calendarBox.set(Calendar.DAY_OF_MONTH, day)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            selectedDate = dateFormat.format(calendarBox.time)
            updateText(calendarBox)
        }

        //Mostra o calendario para o usuario escolher as datas
        dataButton.setOnClickListener {
            DatePickerDialog(
                this,
                dateBox,
                calendarBox.get(Calendar.YEAR),
                calendarBox.get(Calendar.MONTH),
                calendarBox.get(Calendar.DAY_OF_MONTH)
            ).show()
        }


        //Escolhe o Array de opções e aplica na UI
        ArrayAdapter.createFromResource(
            this,
            R.array.WeeklyExercise,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinnerWeek.adapter = adapter
        }

        spinnerWeek.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedWeeklyExercise = parent?.getItemAtPosition(position)


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                val warning = findViewById<TextView>(R.id.emptyFieldWeeklyExercise)
                warning.visibility = View.VISIBLE
                Snackbar.make(saveBtn, "Preencha os campos obrigatórios", Snackbar.LENGTH_LONG)
                    .show()
            }

        }

        //Escolhe o Array de opções e aplica na UI
        ArrayAdapter.createFromResource(
            this,
            R.array.ExerciseType,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerExercise.adapter = adapter
        }

        spinnerExercise.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedExerciseType = parent?.getItemAtPosition(position)
                SpinnerTypeString = selectedExerciseType.toString()


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                val warning = findViewById<TextView>(R.id.emptyFieldExerciseType)
                warning.visibility = View.VISIBLE
                Snackbar.make(saveBtn, "Preencha os campos obrigatórios", Snackbar.LENGTH_LONG)
                    .show()
            }

        }



        resultIntent = Intent()

        saveBtn = findViewById(R.id.save_btn)
        saveBtn.setOnClickListener {

            println(genderRadioGroup.checkedRadioButtonId)

            if (userData != null) {
                updateUser()
            } else {
                insertUser()
            }

        }

        //Alongamento

            val switchAlongamento = findViewById<SwitchCompat>(R.id.switch_alongamentos)
            val spinnerStretch = findViewById<Spinner>(R.id.spinner_alongamento)
            spinnerStretch.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (switchAlongamento.isChecked){
                        timeBoxStretch(position)
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }



        val selectDataStretchTextView = findViewById<TextView>(R.id.alongamento_data_tv)
        val mondayCheckBox = findViewById<CheckBox>(R.id.monday_rb)
        val tuesdayCheckBox = findViewById<CheckBox>(R.id.tues_rb)
        val wednesdayCheckBox = findViewById<CheckBox>(R.id.wed_rb)
        val thursdayCheckBox = findViewById<CheckBox>(R.id.thurs_rb)
        val fridayCheckBox = findViewById<CheckBox>(R.id.friday_rb)
        val saturdayCheckBox = findViewById<CheckBox>(R.id.sat_rb)
        val sundayCheckBox = findViewById<CheckBox>(R.id.sunday_rb)
        val dailyStretchTextView = findViewById<TextView>(R.id.repetição_dia_tv)
        val dailyStretchSpinner = findViewById<CardView>(R.id.alogangamento_spinner)
        val selectTimeTextView = findViewById<TextView>(R.id.stretch_time_tv)
        val selectTimeOne = findViewById<CardView>(R.id.stretch_time1_cardView)



        switchAlongamento.setOnClickListener {

            if (switchAlongamento.isChecked) {
                selectDataStretchTextView.visibility = View.VISIBLE
                dailyStretchTextView.visibility = View.VISIBLE
                dailyStretchSpinner.visibility = View.VISIBLE
                selectTimeTextView.visibility = View.VISIBLE
                selectTimeOne.visibility = View.VISIBLE
                mondayCheckBox.visibility = View.VISIBLE
                tuesdayCheckBox.visibility = View.VISIBLE
                wednesdayCheckBox.visibility = View.VISIBLE
                thursdayCheckBox.visibility = View.VISIBLE
                fridayCheckBox.visibility = View.VISIBLE
                saturdayCheckBox.visibility = View.VISIBLE
                sundayCheckBox.visibility = View.VISIBLE



            } else {
                selectDataStretchTextView.visibility = View.GONE
                dailyStretchTextView.visibility = View.GONE
                dailyStretchSpinner.visibility = View.GONE
                selectTimeTextView.visibility = View.GONE
                selectTimeOne.visibility = View.GONE
                mondayCheckBox.visibility = View.GONE
                tuesdayCheckBox.visibility = View.GONE
                wednesdayCheckBox.visibility = View.GONE
                thursdayCheckBox.visibility = View.GONE
                fridayCheckBox.visibility = View.GONE
                saturdayCheckBox.visibility = View.GONE
                sundayCheckBox.visibility = View.GONE

            }

        }







    }

    private fun timeBoxStretch(position:Int){
        val selectTimeTwo = findViewById<CardView>(R.id.stretch_time2_cardView)
        val selectTimeThree = findViewById<CardView>(R.id.stretch_time3_cardView)
        when (position) {
            1 -> {
                selectTimeTwo.visibility = View.VISIBLE
                selectTimeThree.visibility = View.GONE
            }
            2 -> {
                selectTimeTwo.visibility = View.VISIBLE
                selectTimeThree.visibility = View.VISIBLE
            }
            else -> {
                selectTimeTwo.visibility = View.GONE
                selectTimeThree.visibility = View.GONE
            }
        }
    }


            private fun updateUser() {
        val birth = if (selectedDate.isNullOrEmpty()) userData!!.birth else selectedDate
        val newUser = User(
            name = edtTextName.text.toString(),
            email = userData?.email.toString(),
            password = edtTextPassword.text.toString(),
            birth = birth.toString(),
            weight = edtTextWeight.text.toString(),
            height = edtHeight.text.toString(),
            gender = gender,
            goal = goal,
            weeklyExercise = SpinnerWeekString,
            exerciseType = SpinnerTypeString
        )

        if (newUser.name.isNotEmpty() && newUser.email.isNotEmpty() &&
            newUser.password.isNotEmpty() && newUser.birth.isNotEmpty() && newUser.weight.isNotEmpty() &&
            newUser.height.isNotEmpty() && newUser.gender.isNotEmpty() && newUser.goal.isNotEmpty() && newUser.weeklyExercise.isNotEmpty() && newUser.exerciseType.isNotEmpty()
        ) {
            if (isPasswordValid(edtTextPassword.text.toString())) {

                lifecycleScope.launch {
                    withContext(IO) {
                        dao.update(newUser)
                    }
                }

                userData.apply {
                    this?.name = newUser.name
                    this?.email = newUser.email
                    this?.password = newUser.password
                    this?.birth = newUser.birth
                    this?.weight = newUser.weight
                    this?.height = newUser.height
                    this?.gender = newUser.gender
                    this?.goal = newUser.goal
                    this?.weeklyExercise = newUser.weeklyExercise
                    this?.exerciseType = newUser.exerciseType
                }


                if (source == SOURCE_MAIN) {
                    setResult(RESULT_REGISTER_SUCCESSFUL_MAIN, resultIntent)
                }
                finish()

            } else if (!isPasswordValid(edtTextPassword.text.toString())) {
                passwordWarning.visibility = View.VISIBLE
                Snackbar.make(saveBtn, "Senha inválida", Snackbar.LENGTH_LONG).show()
            }

        } else {
            showEmptyFieldMessage(edtTextName.text.isEmpty(), R.id.emptyFieldName)
            showEmptyFieldMessage(edtTextEmail.text.isEmpty(), R.id.emptyFieldEmail)
            showEmptyFieldMessage(edtTextPassword.text.isEmpty(), R.id.emptyFieldPassword)
            showEmptyFieldMessage(edtTextWeight.text.isEmpty(), R.id.emptyFieldWeight)
            showEmptyFieldMessage(edtHeight.text.isEmpty(), R.id.emptyFieldHeight)

        }
    }

    fun insertUser(){
        val user = User(
            name = edtTextName.text.toString(),
            email = edtTextEmail.text.toString(),
            password = edtTextPassword.text.toString(),
            birth = selectedDate.toString(),
            weight = edtTextWeight.text.toString(),
            height = edtHeight.text.toString(),
            gender = gender,
            goal = goal,
            weeklyExercise = SpinnerWeekString,
            exerciseType = SpinnerTypeString
        )


        if (user.name.isNotEmpty() && user.email.isNotEmpty() &&
            user.password.isNotEmpty() && user.birth.isNotEmpty() && user.weight.isNotEmpty() &&
            user.height.isNotEmpty() && user.gender.isNotEmpty() && user.goal.isNotEmpty() && user.weeklyExercise.isNotEmpty() && user.exerciseType.isNotEmpty()
        ) {
            if (isPasswordValid(edtTextPassword.text.toString())) {

                lifecycleScope.launch {
                    withContext(IO) {
                        dao.insert(user)
                    }
                }
                if (source == SOURCE_LOGIN) {
                    setResult(RESULT_REGISTER_SUCCESSFUL_LOGIN, resultIntent)
                }
                finish()

            } else if (!isPasswordValid(edtTextPassword.text.toString())) {
                passwordWarning.visibility = View.VISIBLE
                Snackbar.make(saveBtn, "Senha inválida", Snackbar.LENGTH_LONG).show()
            }

        } else {
            showEmptyFieldMessage(edtTextName.text.isEmpty(), R.id.emptyFieldName)
            showEmptyFieldMessage(edtTextEmail.text.isEmpty(), R.id.emptyFieldEmail)
            showEmptyFieldMessage(edtTextPassword.text.isEmpty(), R.id.emptyFieldPassword)
            showEmptyFieldMessage(selectedDate == null, R.id.emptyFieldDate)
            showEmptyFieldMessage(edtTextWeight.text.isEmpty(), R.id.emptyFieldWeight)
            showEmptyFieldMessage(edtHeight.text.isEmpty(), R.id.emptyFieldHeight)
            showEmptyFieldMessage(genderRadioGroup.checkedRadioButtonId == -1, R.id.emptyFieldGender)
            showEmptyFieldMessage(goalsRadioGroup.checkedRadioButtonId == -1, R.id.emptyFieldGoal)

        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REGISTER_REQUEST_CODE) {
            if (resultCode == RESULT_REGISTER_SUCCESSFUL_LOGIN) {
                val loginIntent = Intent(this, MainActivityLogin::class.java)
                startActivity(loginIntent)
                finish()
            } else if (resultCode == RESULT_REGISTER_SUCCESSFUL_MAIN) {
                val mainIntent = Intent(this, TelaPrincipalBotoesCalculo::class.java)
                startActivity(mainIntent)
                finish()
            }
        }
    }

    //Se os RadioButton forem selecionados, será setado uma cor
    fun onRadioButtonClicked(view: View) {



        val isSelected = (view as AppCompatRadioButton).isChecked
        when (view.id) {
            R.id.rb_female -> {
                if (isSelected) {
                    val radioButtonMale = findViewById<RadioButton>(R.id.rb_male)
                    radioButtonMale.setTextColor(Color.GRAY)


                }
            }

            R.id.rb_male -> {
                if (isSelected) {
                    val radioButtonFemale = findViewById<RadioButton>(R.id.rb_female)
                    radioButtonFemale.setTextColor(Color.GRAY)
                }
            }

            R.id.rb_keep -> {
                if (isSelected) {
                    val radioButtonLose = findViewById<RadioButton>(R.id.rb_lose)
                    radioButtonLose.setTextColor(Color.GRAY)
                    val radioButtonGain = findViewById<RadioButton>(R.id.rb_gain)
                    radioButtonGain.setTextColor(Color.GRAY)
                }
            }

            R.id.rb_gain -> {
                if (isSelected) {
                    val radioButtonLose = findViewById<RadioButton>(R.id.rb_lose)
                    radioButtonLose.setTextColor(Color.GRAY)
                    val radioButtonKeep = findViewById<RadioButton>(R.id.rb_keep)
                    radioButtonKeep.setTextColor(Color.GRAY)
                }
            }

            R.id.rb_lose -> {
                if (isSelected) {
                    val radioButtonGain = findViewById<RadioButton>(R.id.rb_gain)
                    radioButtonGain.setTextColor(Color.GRAY)
                    val radioButtonKeep = findViewById<RadioButton>(R.id.rb_keep)
                    radioButtonKeep.setTextColor(Color.GRAY)
                }
            }

        }
    }



    //Verifica se a senha é valida
    private fun isPasswordValid(password: String): Boolean {
        val hasUpperCase = password.any {
            it.isUpperCase()
        }
        val hasLowerCase = password.any {
            it.isLowerCase()
        }
        val hasDigit = password.any {
            it.isDigit()
        }
        val isLengthValid = password.length >= 8

        return hasUpperCase && hasLowerCase && hasDigit && isLengthValid
    }

    //Verifica se os campos estão vazios e seta uma mensagem
    private fun showEmptyFieldMessage(isEmpty: Boolean, emptyFieldId: Int) {
        val emptyField = findViewById<TextView>(emptyFieldId)
        if (isEmpty) {
            emptyField.visibility = View.VISIBLE
            Snackbar.make(saveBtn, "Preencha os campos obrigatórios", Snackbar.LENGTH_LONG).show()
        } else {
            emptyField.visibility = View.GONE
        }
    }

    //Atualiza o box da Data
    private fun updateText(calendar: Calendar) {
        val dateFormat = "dd/MM/yyyy"
        val simple = SimpleDateFormat(dateFormat, Locale.UK)
        dataTextView.text = simple.format(calendar.time)
        dataTextView.setTextColor(Color.BLACK)
    }

    //Mostra uma mensagem ao clicar no botão de salvar.
    private fun showAlertDialog() {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Tem certeza que deseja sair?")
            .setMessage("Seus dados não serão salvos.")
            .setPositiveButton("Sim") { dialog, which ->
                finish()
            }
            .setNegativeButton("Não") { dialog, which -> }
            .create()
        alertDialog.show()
    }

}
