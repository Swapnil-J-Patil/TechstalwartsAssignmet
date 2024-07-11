package com.swapnil.techstalwartsassignmet.ui.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import com.swapnil.techstalwartsassignmet.R
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.swapnil.techstalwartsassignmet.ui.theme.blue
import com.swapnil.techstalwartsassignmet.ui.theme.lightBlue
import com.swapnil.techstalwartsassignmet.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel,
) {
    val context = LocalContext.current
    var loginButtonClicked by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.login),
                contentDescription = "login Image",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .size(200.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = viewModel.email.value,
                onValueChange = { viewModel.email.value = it },
                label = { Text("Email", color = Color.Black) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 17.sp,
                    color = Color.Black
                ),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = lightBlue,
                    unfocusedBorderColor = lightBlue,
                    focusedLabelColor = lightBlue,
                    cursorColor = lightBlue
                ),
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = viewModel.password.value,
                onValueChange = { viewModel.password.value = it },
                label = { Text("Password", color = Color.Black) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 17.sp,
                    color = Color.Black
                ),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = lightBlue,
                    unfocusedBorderColor = lightBlue,
                    focusedLabelColor = lightBlue,
                    cursorColor = lightBlue
                ),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    viewModel.login()
                    loginButtonClicked = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = lightBlue),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login", color = Color.Black)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Spacer(modifier = Modifier.height(30.dp))
        }
    }

    LaunchedEffect(viewModel.token.value) {
        if (viewModel.token.value != null) {
            loginButtonClicked = false
            viewModel.errorMessage.value = ""
            navController.navigate("user_screen")
        }
    }

    LaunchedEffect(viewModel.errorMessage.value) {
        if (viewModel.errorMessage.value.isNotEmpty() && loginButtonClicked) {
            Toast.makeText(context, "Wrong Credentials!", Toast.LENGTH_SHORT).show()
            loginButtonClicked = false
        }
    }
}

