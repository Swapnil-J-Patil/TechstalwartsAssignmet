package com.swapnil.techstalwartsassignmet.ui.view
import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import coil.compose.rememberImagePainter
import com.swapnil.techstalwartsassignmet.ui.theme.blue
import com.swapnil.techstalwartsassignmet.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.LatLng
import com.swapnil.techstalwartsassignmet.ui.theme.lightBlue
import com.swapnil.techstalwartsassignmet.R
import com.swapnil.techstalwartsassignmet.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
    navController: NavController,
    loginViewModel: LoginViewModel,
    viewModel: UserViewModel = viewModel()
) {
    val users by viewModel.users.collectAsState()
    val context = LocalContext.current
    var searchClicked by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    LaunchedEffect(searchClicked) {
        if (searchClicked) {
            viewModel.filterUsers(searchText)
            searchClicked = false // Reset the flag
        }
    }

    LaunchedEffect(searchText) {
        if (searchText.isEmpty() && searchClicked) {
            viewModel.fetchAllUsers()
            searchClicked = false // Reset the flag
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 10.dp, end = 10.dp)
                .background(Color.White)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp, top = 28.dp)
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                Text(
                    text = "Hello Users!",
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        searchClicked = true
                    },
                    color = Color.Black,
                )

                Text(
                    text = "Logout",
                    style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Medium),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        loginViewModel.logout()
                        navController.navigate("login_screen")
                    },
                    color = Color.Black,
                )
            }
            Card(
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 15.dp)
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 12.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(0.dp)
                        .fillMaxWidth()
                        .background(Color.White)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = Color.Gray,
                        modifier = Modifier
                            .padding(start = 12.dp, end = 8.dp)
                            .size(30.dp)
                    )
                    TextField(
                        value = searchText,
                        onValueChange = {
                            searchText = it
                            searchClicked = true
                        },
                        modifier = Modifier
                            .weight(1f)
                            .background(Color.White), // Set background color to white
                        placeholder = {
                            Text(
                                "Search profiles",
                                color = Color.Black,
                                fontSize = 18.sp
                            )
                        },
                        singleLine = true, // Ensure single line input
                        colors = TextFieldDefaults.textFieldColors(
                            focusedTextColor = Color.Black,
                            containerColor = Color.White,
                            focusedIndicatorColor = Color.Transparent, // Remove bottom line when focused
                            unfocusedIndicatorColor = Color.Transparent // Remove bottom line when unfocused
                        )

                    )

                    Text(
                        text = "Search  ",
                        style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            searchClicked = true
                        },
                        color = lightBlue,
                    )
                }
            }

            LazyColumn {
                items(users) { user ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(Color.White)
                    ) {
                        Image(
                            painter = rememberImagePainter(user.avatar),
                            contentDescription = null,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                user.first_name + " " + user.last_name,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(user.email, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
        FloatingActionButton(
            onClick = {
                val intent = Intent(context, MapScreen::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 15.dp, end = 10.dp)
                .size(50.dp),
            containerColor = Color.White,
            shape = CircleShape
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_location_pin_24),
                contentDescription = "Expand/Collapse",
                tint = lightBlue,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}


