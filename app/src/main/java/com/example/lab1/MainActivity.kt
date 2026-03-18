package com.example.lab1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val radioGroup = findViewById<RadioGroup>(R.id.radioGroupYears)
        val btnClear = findViewById<Button>(R.id.btnClear)
        val btnOkYears = findViewById<Button>(R.id.btnOkYears)
        val etSurname = findViewById<AutoCompleteTextView>(R.id.etSurname)
        val btnOkSurname = findViewById<Button>(R.id.btnOkSurname)
        val txtResult = findViewById<TextView>(R.id.txtResult)

        val authorsByYears = mapOf(
            "До 1950" to listOf("Тарас Шевченко", "Іван Франко"),
            "1950–2000" to listOf("Ліна Костенко", "Михайло Коцюбинський"),
            "Після 2000" to listOf("Сергій Жадан", "Юрій Андрухович")
        )

        val booksByAuthor = mapOf(
            "Тарас Шевченко" to listOf("Кобзар (1840)", "Гайдамаки (1841)"),
            "Іван Франко" to listOf("Захар Беркут (1883)", "Мойсей (1905)"),
            "Ліна Костенко" to listOf("Маруся Чурай (1979)", "Берестечко (1999)"),
            "Михайло Коцюбинський" to listOf("Тіні забутих предків (1911)", "Intermezzo (1908)"),
            "Сергій Жадан" to listOf("Ворошиловград (2010)", "Інтернат (2017)"),
            "Юрій Андрухович" to listOf("Рекреації (1992)", "Московіада (1993)")
        )

        // Підказка прізвища при введенні
        val allSurnames = booksByAuthor.keys.toList()

        val surnameAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            allSurnames
        )

        etSurname.setAdapter(surnameAdapter)
        etSurname.threshold = 1  // Підказка після 1 літери

        // Пошук по рокам
        btnOkYears.setOnClickListener {
            val selectedId = radioGroup.checkedRadioButtonId
            if (selectedId == -1) {
                Toast.makeText(this, "Оберіть діапазон років", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedYear = findViewById<RadioButton>(selectedId).text.toString()
            val authors = authorsByYears[selectedYear] ?: emptyList()

            if (authors.isEmpty()) {
                txtResult.text = "Немає авторів для обраного діапазону"
                return@setOnClickListener
            }

            val result = StringBuilder("Автори та їх книги:\n\n")
            for (author in authors) {
                result.append("$author:\n")
                booksByAuthor[author]?.forEach { book ->
                    result.append(" • $book\n")
                }
                result.append("\n")
            }

            txtResult.text = result.toString()
        }

        btnClear.setOnClickListener {
            radioGroup.clearCheck()
        }

        // Пошук за прізвищем
        btnOkSurname.setOnClickListener {
            val surname = etSurname.text.toString().trim()

            // Обробка помилок
            when {
                surname.isEmpty() -> {
                    Toast.makeText(this, "Введіть прізвище", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                surname.any { it.isDigit() } -> {
                    Toast.makeText(this, "Прізвище не може містити цифри", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            // Пошук автора
            val author = booksByAuthor.keys.find { it.equals(surname, ignoreCase = true) }

            if (author == null) {
                txtResult.text = "Автора \"$surname\" не знайдено"
                return@setOnClickListener
            }

            val books = booksByAuthor[author]!!
            txtResult.text = "Книги автора $author:\n" + books.joinToString("\n")
        }
    }
}
