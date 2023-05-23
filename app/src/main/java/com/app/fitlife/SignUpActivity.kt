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
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.appcompat.widget.Toolbar
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


    private lateinit var dataTextView: TextView
    private lateinit var dataButton: CardView
    private lateinit var saveBtn: Button
    private lateinit var dao: UserDao
    private var date: String? = null
    var SpinnerWeek: String = ""
    var SpinnerType: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // recuperação dos dados no menu editar perfil
        val passwordWarning = findViewById<TextView>(R.id.warning_tv)
        val edtTextName = findViewById<EditText>(R.id.name_edt_text)
        val edtTextEmail = findViewById<EditText>(R.id.email_edt_text)
        val edtTextPassword = findViewById<EditText>(R.id.edt_text_password)
        val edtTextWeight = findViewById<EditText>(R.id.weight_edt_text)
        val edtHeight = findViewById<EditText>(R.id.height_edt_text)

        //CheckBox Gender
        val genderRadioGroup = findViewById<RadioGroup>(R.id.gender_radio_group)
        val radioButtonFemale = findViewById<RadioButton>(R.id.rb_female)

        //  função para recuperação dos dados no menu editar perfil
        val userData = intent.getSerializableExtra("EXTRA_USER_DATA") as User?
        if (userData != null) {
            edtTextName.setText(userData?.name)
            edtTextEmail.setText(userData?.email)
            edtTextPassword.setText(userData?.password)
            edtTextWeight.setText(userData?.weight)
            edtHeight.setText(userData?.height)
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
        dataTextView = findViewById(R.id.date_tv)

        //Guarda a data escolhida pelo usuario
        val calendarBox = Calendar.getInstance()
        val dateBox = DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
            calendarBox.set(Calendar.YEAR, year)
            calendarBox.set(Calendar.MONTH, month)
            calendarBox.set(Calendar.DAY_OF_MONTH, day)
            //Atualiza o texto da EditText
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


        //CheckBox Goals
        val radioButtonKeep = findViewById<RadioButton>(R.id.rb_keep)
        val radioButtonGain = findViewById<RadioButton>(R.id.rb_gain)
        val goalsRadioGroup = findViewById<RadioGroup>(R.id.rg_goal)


        //Spinner
        val spinnerWeek = findViewById<Spinner>(R.id.spinner_weekly)
        val spinnerExercise = findViewById<Spinner>(R.id.spinner_exercise_type)

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
                val selectedWeeklyExercise = parent?.getItemAtPosition(position).toString()
                //Guarda a opção escolhida pelo usuario
                SpinnerWeek = selectedWeeklyExercise
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
                val selectedExerciseType = parent?.getItemAtPosition(position).toString()
                //Guarda a opção escolhida pelo usuario
                SpinnerType = selectedExerciseType
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                val warning = findViewById<TextView>(R.id.emptyFieldExerciseType)
                warning.visibility = View.VISIBLE
                Snackbar.make(saveBtn, "Preencha os campos obrigatórios", Snackbar.LENGTH_LONG)
                    .show()
            }

        }


        val nameText = edtTextName.text
        val emailText = edtTextEmail.text
        val passwordText = edtTextPassword.text
        val weightText = edtTextWeight.text
        val heightText = edtHeight.text
        val gender = if (radioButtonFemale.isSelected) "Feminino" else "Masculino"
        val goal =
            if (radioButtonKeep.isSelected) "Manter" else if (radioButtonGain.isSelected) "Ganhar" else "Emagrecer"


        saveBtn = findViewById(R.id.save_btn)

        saveBtn.setOnClickListener {

            val user = User(
                nameText.toString(),
                emailText.toString(),
                passwordText.toString(),
                date.toString(),
                weightText.toString(),
                heightText.toString(),
                gender,
                goal,
                SpinnerWeek,
                SpinnerType
            )


            val intent = Intent(this, MainActivityLogin::class.java)
            startActivity(intent)


            //Verifica se os campos não estão vazios
            if (user.name.isNotEmpty() && user.email.isNotEmpty() &&
                user.password.isNotEmpty() && user.birth.isNotEmpty() && user.weight.isNotEmpty() &&
                user.weight.isNotEmpty() && user.gender.isNotEmpty() && user.goal.isNotEmpty() && user.weeklyExercise.isNotEmpty() && user.weeklyExercise.isNotEmpty()
            ) {
                //Verifica se a senha é valida
                if (isPasswordValid(passwordText.toString())) {

                    lifecycleScope.launch {
                        withContext(IO) {
                            dao.insert(user)
                        }
                    }

                } else if (!isPasswordValid(passwordText.toString())) {
                    passwordWarning.visibility = View.VISIBLE
                    Snackbar.make(saveBtn, "Senha inválida", Snackbar.LENGTH_LONG).show()
                }
                //Se os campos estiverem vazios, será mostrado uma mensagem de alerta
            } else {
                showEmptyFieldMessage(nameText.isEmpty(), R.id.emptyFieldName)
                showEmptyFieldMessage(emailText.isEmpty(), R.id.emptyFieldEmail)
                showEmptyFieldMessage(passwordText.isEmpty(), R.id.emptyFieldPassword)
                showEmptyFieldMessage(date == null, R.id.emptyFieldDate)
                showEmptyFieldMessage(weightText.isEmpty(), R.id.emptyFieldWeight)
                showEmptyFieldMessage(heightText.isEmpty(), R.id.emptyFieldHeight)
                showEmptyFieldMessage(
                    genderRadioGroup.checkedRadioButtonId == -1,
                    R.id.emptyFieldGender
                )
                showEmptyFieldMessage(
                    goalsRadioGroup.checkedRadioButtonId == -1,
                    R.id.emptyFieldGoal
                )

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
        val dateString = simple.format(calendar.time).toString()
        date = dateString
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