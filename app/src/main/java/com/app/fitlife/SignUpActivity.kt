package com.app.fitlife

import android.app.DatePickerDialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.cardview.widget.CardView
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class SignUpActivity : AppCompatActivity() {

    private lateinit var dataTextView: TextView
    private lateinit var dataButton: CardView
    private lateinit var saveBtn: Button
    private var date:String? = null
    private var selectedDate: Date? = null
    var userSelectionWeek : String =""
    var userSelectionType : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


        //Calendario
        dataButton = findViewById(R.id.data_cardview)
        dataTextView = findViewById(R.id.date_tv)

        val calendarBox = Calendar.getInstance()
        val dateBox = DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
            calendarBox.set(Calendar.YEAR, year)
            calendarBox.set(Calendar.MONTH, month)
            calendarBox.set(Calendar.DAY_OF_MONTH, day)
            selectedDate = calendarBox.time
            updateText(calendarBox)
        }
        dataButton.setOnClickListener {
            DatePickerDialog(
                this,
                dateBox,
                calendarBox.get(Calendar.YEAR),
                calendarBox.get(Calendar.MONTH),
                calendarBox.get(Calendar.DAY_OF_MONTH)
            ).show()
        }


        //CheckBox Gender
        val genderRadioGroup = findViewById<RadioGroup>(R.id.gender_radio_group)
        val radioButtonMale = findViewById<RadioButton>(R.id.rb_male)
        val radioButtonFemale = findViewById<RadioButton>(R.id.rb_female)
        genderRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_female -> {

                    radioButtonMale.setTextColor(Color.GRAY)
                }

                R.id.rb_male -> {

                    radioButtonFemale.setTextColor(Color.GRAY)
                }
            }
        }
        //CheckBox Goals
        val radioButtonKeep = findViewById<RadioButton>(R.id.rb_keep)
        val radioButtonLose = findViewById<RadioButton>(R.id.rb_lose)
        val radioButtonGain = findViewById<RadioButton>(R.id.rb_gain)
        val goalsRadioGroup = findViewById<RadioGroup>(R.id.rg_goal)

        genderRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_keep -> {
                    radioButtonGain.setTextColor(Color.GRAY)
                    radioButtonGain.setTextColor(Color.GRAY)

                }

                R.id.rb_gain -> {
                    radioButtonKeep.setTextColor(Color.GRAY)
                    radioButtonLose.setTextColor(Color.GRAY)
                }

                R.id.rb_lose -> {
                    radioButtonKeep.setTextColor(Color.GRAY)
                    radioButtonGain.setTextColor(Color.GRAY)
                }
            }
        }



        //Spinner
        val spinnerWeek = findViewById<Spinner>(R.id.spinner_weekly)
        val spinnerExercise = findViewById<Spinner>(R.id.spinner_exercise_type)

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

        spinnerWeek.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedWeeklyExercise = parent?.getItemAtPosition(position).toString()
                userSelectionWeek = selectedWeeklyExercise
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                val warning = findViewById<TextView>(R.id.emptyFieldWeeklyExercise)
                warning.visibility = View.VISIBLE
                Snackbar.make(saveBtn, "Preencha os campos obrigatórios", Snackbar.LENGTH_LONG).show()
            }

        }

        ArrayAdapter.createFromResource(
            this,
            R.array.ExerciseType,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinnerExercise.adapter = adapter
        }

        spinnerExercise.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedExerciseType = parent?.getItemAtPosition(position).toString()
                userSelectionType = selectedExerciseType
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                val warning = findViewById<TextView>(R.id.emptyFieldExerciseType)
                warning.visibility = View.VISIBLE
                Snackbar.make(saveBtn, "Preencha os campos obrigatórios", Snackbar.LENGTH_LONG).show()
            }

        }

        val passwordWarning = findViewById<TextView>(R.id.warning_tv)
        val edtTextName = findViewById<EditText>(R.id.name_edt_text)
        val edtTextEmail = findViewById<EditText>(R.id.email_edt_text)
        val edtTextPassword = findViewById<EditText>(R.id.edt_text_password)
        val edtTextWeight = findViewById<EditText>(R.id.weight_edt_text)
        val edtHeight = findViewById<EditText>(R.id.height_edt_text)



        val nameText = edtTextName.text
        val emailText = edtTextEmail.text
        val passwordText = edtTextPassword.text
        val weightText = edtTextWeight.text
        val heightText = edtHeight.text

        saveBtn = findViewById(R.id.save_btn)

        saveBtn.setOnClickListener {
            showEmptyFieldMessage(nameText.isEmpty(), R.id.emptyFieldName)
            showEmptyFieldMessage(emailText.isEmpty(), R.id.emptyFieldEmail)
            showEmptyFieldMessage(passwordText.isEmpty(), R.id.emptyFieldPassword)
            showEmptyFieldMessage(date == null, R.id.emptyFieldDate)
            showEmptyFieldMessage(weightText.isEmpty(), R.id.emptyFieldWeight)
            showEmptyFieldMessage(heightText.isEmpty(), R.id.emptyFieldHeight)
            showEmptyFieldMessage(genderRadioGroup.checkedRadioButtonId == -1, R.id.emptyFieldGender)
            showEmptyFieldMessage(goalsRadioGroup.checkedRadioButtonId == -1, R.id.emptyFieldGoal)



        }

    }

    fun onRadioButtonClicked(view: View) {
        val isSelected = (view as AppCompatRadioButton).isChecked
        when (view.id) {
            R.id.rb_female -> {
                if (isSelected) {
                    val radioButtonHomem = findViewById<RadioButton>(R.id.rb_male)
                    radioButtonHomem.setTextColor(Color.GRAY)


                }
            }

            R.id.rb_male -> {
                if (isSelected) {
                    val radioButtonMulher = findViewById<RadioButton>(R.id.rb_female)
                    radioButtonMulher.setTextColor(Color.GRAY)
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


    private fun showEmptyFieldMessage(isEmpty: Boolean, emptyFieldId: Int) {
        val emptyField = findViewById<TextView>(emptyFieldId)
        if (isEmpty) {
            emptyField.visibility = View.VISIBLE
            Snackbar.make(saveBtn, "Preencha os campos obrigatórios", Snackbar.LENGTH_LONG).show()
        } else {
            emptyField.visibility = View.GONE
        }
    }

    private fun updateText(calendar: Calendar) {
        val dateFormat = "dd/MM/yyyy"
        val simple = SimpleDateFormat(dateFormat, Locale.UK)
        dataTextView.text = simple.format(calendar.time)
        dataTextView.setTextColor(Color.BLACK)
        val dateString = simple.format(calendar.time)
        date = dateString
    }
}