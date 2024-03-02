package com.example.myapplication

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.controller.NextQuestion
import com.example.myapplication.controller.ScoreController
import com.example.myapplication.model.AllQuestions
import com.example.myapplication.model.UserID
import com.example.myapplication.ui.theme.MyApplicationTheme
import androidx.compose.foundation.layout.Arrangement


class MainActivity : ComponentActivity() {
    private val userID = UserID()

    private var scoreValue by mutableStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            scoreValue = it.getInt("scoreValue")
        }

        userID.setName("Toyi Shimizu")

        setContent {
            MyApplicationTheme {
                Column {
                    Text("Welcome to the quiz app!")
                    Text("${userID.getName()}")
                    QuizComponent(userID)
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("scoreValue", scoreValue)
        super.onSaveInstanceState(outState)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }
}



@Composable
fun QuizComponent(userID: UserID) {
    val allQuestions = AllQuestions()
    val nextQuestion = NextQuestion()
    val score = ScoreController(userID.getName()!!)

    var questionNumber by remember { mutableStateOf(0) }
    var question by remember { mutableStateOf(allQuestions.getQuestion(questionNumber).questionText) }
    var answer by remember { mutableStateOf(allQuestions.getQuestion(questionNumber).answer) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(15.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.Center, // Center the content vertically
                horizontalAlignment = Alignment.CenterHorizontally // Center the content horizontally
            ) {
                Text(
                    text = question,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Button(
                    onClick = { if (answer) score.incrementScore(allQuestions.getQuestion(questionNumber).difficulty) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .height(60.dp)
                ) {
                    Text("True")
                }
                Button(
                    onClick = { if (!answer) score.incrementScore(allQuestions.getQuestion(questionNumber).difficulty) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                ) {
                    Text("False")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Score: ${score.getScore()}",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }

        Button(
            onClick = {
                questionNumber = nextQuestion.getNextLinearQuestion()
                question = allQuestions.getQuestion(questionNumber).questionText
                answer = allQuestions.getQuestion(questionNumber).answer
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Next Question")
        }
    }
}
