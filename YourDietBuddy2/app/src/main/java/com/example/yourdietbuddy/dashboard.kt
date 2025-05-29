package com.example.yourdietbuddy
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController


val foodList = listOf(
    FoodItem("Burger", "Grill patty, add veggies, top with bun", "Beef, Lettuce, Tomato, Bun", 540, R.drawable.burger),
    FoodItem("Fruit Bowl", "Cut fruits and mix", "Mango, Banana, Apple, Yogurt", 220, R.drawable.fruitbowl),
    FoodItem("Pizza", "Bake with toppings", "Dough, Cheese, Tomato Sauce", 480, R.drawable.pizza),
    FoodItem("Salad", "Mix greens and dress", "Lettuce, Tomato, Olive oil", 150, R.drawable.salad),
    FoodItem("Sushi", "Roll with rice and fish", "Rice, Nori, Fish", 320, R.drawable.sushi),
)

@Composable
fun DashboardScreen(navController: NavHostController) {
    val gradientBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFFF5F7FA), Color(0xFFC3CFE2))
    )
    val meals = listOf(
        Meal("07:30", "Sarapan", "Pancake + Madu + Buah", 420),
        Meal("12:15", "Makan Siang", "Nasi + Ayam Bakar + Sayur", 650),
        Meal("15:00", "Snack", "Apel + Kacang Almond", 180),
        Meal("19:30", "Makan Malam", "Salad Sayuran + Protein", 320)
    )
    var selectedFood by remember { mutableStateOf<FoodItem?>(null) }
    var showAllFoods by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = { DashboardBottomBar(navController = navController) },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBackground)
                .padding(innerPadding)
        ) {
            if (selectedFood != null) {
                FoodDetailView(food = selectedFood!!, onBack = { selectedFood = null })
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Spacer(Modifier.height(16.dp))
                        Text("Progress Hari Ini", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Color(0xFF2D3748), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Start)
                        Text("26 Mei 2025", fontSize = 16.sp, color = Color(0xFF4A5568), modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp), textAlign = TextAlign.Start)
                        CalorieProgressSection()
                        Spacer(Modifier.height(12.dp))
                        MacroSection()
                        Spacer(Modifier.height(20.dp))
                        Text("Rekomendasi Makanan", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF2D3748), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Start)
                        Spacer(Modifier.height(12.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
                            items(foodList) { food ->
                                Card(
                                    modifier = Modifier
                                        .width(220.dp)
                                        .height(140.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .clickable { selectedFood = food },
                                    elevation = CardDefaults.cardElevation(8.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White)
                                ) {
                                    Image(painter = painterResource(id = food.imageRes), contentDescription = food.name, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                                }
                            }
                        }
                        Spacer(Modifier.height(24.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("Makanan Hari Ini", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF2D3748))
                            Text("Lihat Semua", color = Color(0xFF3182CE), fontSize = 14.sp, modifier = Modifier.clickable { showAllFoods = true })
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                    items(if (showAllFoods) foodList else meals) { item ->
                        if (item is Meal) MealCard(meal = item)
                        else if (item is FoodItem) FoodCard(food = item)
                    }
                    item { Spacer(Modifier.height(80.dp)) }
                }
            }
        }
    }
}

@Composable
fun FoodDetailView(food: FoodItem, onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(food.name, fontWeight = FontWeight.Bold, fontSize = 24.sp)
        Spacer(Modifier.height(16.dp))
        Image(painter = painterResource(id = food.imageRes), contentDescription = null, modifier = Modifier.height(200.dp).clip(RoundedCornerShape(16.dp)), contentScale = ContentScale.Crop)
        Spacer(Modifier.height(16.dp))
        Text("Instruksi: ${food.instructions}", fontSize = 16.sp)
        Text("Bahan: ${food.ingredients}", fontSize = 16.sp)
        Text("Kalori: ${food.calories} kal", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(24.dp))
        Button(onClick = onBack) {
            Text("Kembali")
        }
    }
}

@Composable
fun DashboardBottomBar(navController: NavHostController) {
    NavigationBar(containerColor = Color.White, tonalElevation = 8.dp, modifier = Modifier.height(60.dp)) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Beranda") },
            label = { Text("Beranda", fontSize = 12.sp) },
            selected = true,
            onClick = { }
        )
        NavigationBarItem(
            icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Diary") },
            label = { Text("Diary", fontSize = 12.sp) },
            selected = false,
            onClick = {
                navController.navigate("dashboard") {
                    popUpTo("dashboard") { inclusive = true }

                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Add, contentDescription = "Tambah") },
            label = { Text("Tambah", fontSize = 12.sp) },
            selected = false,
            onClick = { }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profil") },
            label = { Text("Profil", fontSize = 12.sp) },
            selected = false,
            onClick = {
                navController.navigate("profile")
            }
        )
    }
}

@Composable
fun CalorieProgressSection() {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), elevation = CardDefaults.cardElevation(8.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("1,420", fontWeight = FontWeight.Bold, fontSize = 36.sp, color = Color(0xFF3182CE))
            Text("dari 2,000 kal", fontSize = 16.sp, color = Color(0xFF718096), modifier = Modifier.padding(top = 4.dp))
            Spacer(modifier = Modifier.height(16.dp))
            LinearProgressIndicator(
                progress = { 0.71f },
                modifier = Modifier.fillMaxWidth().height(10.dp).clip(RoundedCornerShape(8.dp)),
                color = Color(0xFF3182CE),
                trackColor = Color(0xFFE2E8F0),
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text("580 kal tersisa", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF4A5568))
        }
    }
}

@Composable
fun MacroSection() {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), elevation = CardDefaults.cardElevation(8.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
            MacroItem("85g", "Protein")
            MacroItem("180g", "Karbohidrat")
            MacroItem("35g", "Lemak")
        }
    }
}

@Composable
fun MacroItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color(0xFF2D3748))
        Text(text = label, fontSize = 14.sp, color = Color(0xFF718096), modifier = Modifier.padding(top = 4.dp))
    }
}

@Composable
fun MealCard(meal: Meal) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(6.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Row(modifier = Modifier.padding(20.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(meal.time, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF4A5568))
                Text(meal.type, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF2D3748), modifier = Modifier.padding(top = 4.dp))
                Text(meal.description, fontSize = 14.sp, color = Color(0xFF718096), modifier = Modifier.padding(top = 2.dp))
            }
            Text("${meal.calories} kal", fontWeight = FontWeight.Bold, color = Color(0xFF3182CE), fontSize = 16.sp)
        }
    }
}

@Composable
fun FoodCard(food: FoodItem) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), shape = RoundedCornerShape(12.dp), elevation = CardDefaults.cardElevation(4.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = food.imageRes), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.size(60.dp).clip(CircleShape))
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(food.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("${food.calories} kal", color = Color(0xFF38A169), fontSize = 13.sp)
            }
        }
    }
}

// Models
data class Meal(val time: String, val type: String, val description: String, val calories: Int)
data class FoodItem(val name: String, val instructions: String, val ingredients: String, val calories: Int, val imageRes: Int)