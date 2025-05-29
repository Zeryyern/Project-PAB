package com.example.yourdietbuddy
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import android.content.Intent
import android.net.Uri
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

class ProfileViewModel : ViewModel() {
    var healthProfile by mutableStateOf(
        mapOf(
            "Kondisi Medis" to "",
            "Alergi" to "",
            "Diet Khusus" to ""
        )
    )
        private set

    var goalTarget by mutableStateOf(
        mapOf(
            "Kalori Harian" to "",
            "Berat Ideal" to "",
            "Timeline" to ""
        )
    )
        private set

    fun updateHealthProfile(newData: Map<String, String>) {
        healthProfile = newData
    }

    fun updateGoalTarget(newData: Map<String, String>) {
        goalTarget = newData
    }
}

@Composable
fun ProfileScreen(navController: NavHostController,viewModel: ProfileViewModel = viewModel()) {
    val context = LocalContext.current
    val logoutDialog = remember { mutableStateOf(false) }
    val selectedNav = remember { mutableStateOf("👤") }
    var showHealthDialog by remember { mutableStateOf(false) }
    var showTargetDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }

    if (showHealthDialog) {
        AlertDialog(
            onDismissRequest = { showHealthDialog = false },
            confirmButton = {},
            dismissButton = {},
            text = {
                HealthProfileSection(
                    initialData = viewModel.healthProfile,
                    onSave = {
                        viewModel.updateHealthProfile(it)
                        showHealthDialog = false
                    },
                    onCancel = { showHealthDialog = false }
                )
            }
        )
    }

    if (showTargetDialog) {
        AlertDialog(
            onDismissRequest = { showTargetDialog = false },
            confirmButton = {},
            dismissButton = {},
            text = {
                GoalTargetSection(
                    initialData = viewModel.goalTarget,
                    onSave = {
                        viewModel.updateGoalTarget(it)
                        showTargetDialog = false
                    },
                    onCancel = { showTargetDialog = false }
                )
            }
        )
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFFE8F4F8), Color(0xFFF0F8FF)),
                    start = Offset.Zero,
                    end = Offset.Infinite
                )
            )
    ) {
        // Konten utama scrollable dengan padding bawah cukup untuk bottom nav + footer
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(Color.White)
                .padding(bottom = 160.dp) // kasih space supaya konten tidak tertutup
        ) {

            HeaderSection {
                Toast.makeText(context, "Membuka pengaturan umum aplikasi", Toast.LENGTH_SHORT).show()
            }

            ProfileHeader(
                name = "John Doe",
                email = "john.doe@email.com",
                onAvatarClick = {
                    Toast.makeText(context, "Fitur ganti foto profil", Toast.LENGTH_SHORT).show()
                }
            )

            InfoCards()

            MenuSection("🧍 Informasi Pribadi", listOf(
                MenuItemData("💊", "Profil Kesehatan", "Kondisi medis, alergi, diet khusus") {
                    showHealthDialog = true
                },
                MenuItemData("🎯", "Target & Tujuan", "Kalori harian, berat ideal, timeline") {
                    showTargetDialog = true
                }
            ))


            MenuSection("💬 Bantuan & Dukungan", listOf(
                MenuItemData("❓", "Pusat Bantuan", "FAQ, panduan penggunaan") {
                    // buka link pusat bantuan
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.ibudanbalita.com/forum/diskusi/Tanya-Jawab-Seputar-Diet"))
                    context.startActivity(intent)
                },
                MenuItemData("ℹ", "Tentang YourDietBuddy", "Versi 2.1.0, syarat & ketentuan") {
                    showAboutDialog = true
                },
                MenuItemData("🚪", "Keluar", "Logout dari akun") {
                    logoutDialog.value = true
                }
            ))
        }

        // BottomNavigationBar dan HeaderSection di bawah layar, tidak ikut scroll
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(Color.White)
                .fillMaxWidth()
                .shadow(4.dp)
        ) {
            BottomNavigationBar(selectedNav,navController)
            HorizontalDivider(thickness = 1.dp, color = Color.LightGray)

        }

        if (logoutDialog.value) {
            AlertDialog(
                onDismissRequest = { logoutDialog.value = false },
                title = { Text("Keluar dari YourDietBuddy") },
                text = {
                    Text("Anda yakin ingin keluar dari akun Anda? Data yang belum disinkronkan mungkin akan hilang.")
                },
                confirmButton = {
                    TextButton(onClick = {
                        logoutDialog.value = false
                        Toast.makeText(context, "Berhasil logout", Toast.LENGTH_SHORT).show()
                    }) {
                        Text("Keluar", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { logoutDialog.value = false }) {
                        Text("Batal")
                    }
                }
            )
        }
        if (showAboutDialog) {
            AlertDialog(
                onDismissRequest = { showAboutDialog = false },
                title = { Text("Tentang YourDietBuddy") },
                text = {
                    Text("YourDietBuddy versi 2.1.0\n\n" +
                            "Aplikasi ini membantu Anda mengatur diet dan target kesehatan dengan mudah.\n\n" +
                            "Syarat dan Ketentuan berlaku.")
                },
                confirmButton = {
                    TextButton(onClick = { showAboutDialog = false }) {
                        Text("Tutup")
                    }
                }
            )
        }

    }
}

@Composable
fun BottomNavigationBar(selected: MutableState<String>,navController: NavHostController) {
    val items = listOf("🏠" to "Beranda", "👤" to "Profil")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                ambientColor = Color.Black.copy(alpha = 0.05f),
                spotColor = Color.Black.copy(alpha = 0.05f),
                clip = false
            )
            .background(Color.White)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        items.forEach { (icon, label) ->
            val isSelected = selected.value == icon
            Column(
                modifier = Modifier
                    .clickable {
                        selected.value = icon
                        // Add navigation logic
                        when (icon) {
                            "🏠" -> navController.navigate("dashboard") {
                                popUpTo("dashboard") { inclusive = true }
                            }
                            "👤" -> navController.navigate("profile") {
                                popUpTo("profile") { inclusive = true }
                            }
                        }
                    }
                    .padding(8.dp)
                    .then(
                        if (isSelected) Modifier
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF4ECDC4), Color(0xFF44A08D))
                                ),
                                RoundedCornerShape(12.dp)
                            ) else Modifier
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(icon, fontSize = 20.sp, color = if (isSelected) Color.White else Color(0xFF95A5A6))
                Text(label, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = if (isSelected) Color.White else Color(0xFF95A5A6))

            }
        }
    }
}


@Composable
fun HeaderSection(onSettingsClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        Brush.linearGradient(listOf(Color(0xFF4ECDC4), Color(0xFF44A08D))),
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("🥗", fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text("YourDietBuddy", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Color(0xFF2C3E50))
        }
        IconButton(
            onClick = onSettingsClick,
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFFF8F9FA), RoundedCornerShape(12.dp))
        ) {
            Text("⚙", fontSize = 18.sp, color = Color(0xFF6C757D))
        }
    }
}


@Composable
fun ProfileHeader(name: String, email: String, onAvatarClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.linearGradient(listOf(Color(0xFF667EEA), Color(0xFF764BA2))))
            .padding(vertical = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(listOf(Color(0xFFFF7675), Color(0xFFFD79A8))))
                    .clickable { onAvatarClick() },
                contentAlignment = Alignment.Center
            ) {
                Text("JD", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(Color(0xFF4ECDC4), CircleShape)
                    .border(2.dp, Color.White, CircleShape)
                    .offset(x = 24.dp, y = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("📷", fontSize = 12.sp)
            }
        }
        Text(name, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text(email, fontSize = 14.sp, color = Color.White.copy(alpha = 0.9f))
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
        ) {
            StatItem("28", "Hari Aktif")
            StatItem("5.2kg", "Progress")
            StatItem("🔥", "Streak")
        }
    }
}

@Composable
fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
        Text(label, fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
    }
}

@Composable
fun InfoCards() {
    // Gunakan mutableStateListOf supaya bisa diubah dinamis dan Compose detect perubahan
    val cards = remember {
        mutableStateListOf(
            Triple("⚖", "72kg", "Berat Badan"),
            Triple("📏", "175cm", "Tinggi Badan"),
            Triple("🎂", "28th", "Usia"),
            Triple("📊", "23.5", "BMI")
        )
    }

    var editingIndex by remember { mutableStateOf<Int?>(null) }
    var inputText by remember { mutableStateOf("") }

    if (editingIndex != null) {
        AlertDialog(
            onDismissRequest = { editingIndex = null },
            title = { Text("Edit ${cards[editingIndex!!].third}") },
            text = {
                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    // update nilai value di list
                    cards[editingIndex!!] = cards[editingIndex!!].copy(second = inputText)
                    editingIndex = null
                }) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                TextButton(onClick = { editingIndex = null }) {
                    Text("Batal")
                }
            }
        )
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .padding(20.dp)
            .height(270.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(cards.size) { index ->
            InfoCard(
                icon = cards[index].first,
                value = cards[index].second,
                label = cards[index].third,
                onClick = {
                    inputText = cards[index].second
                    editingIndex = index
                }
            )
        }
    }
}

fun accentColorFor(label: String): Brush = when (label) {
    "Berat Badan" -> Brush.linearGradient(listOf(Color(0xFF667EEA), Color(0xFF764BA2)))
    "Tinggi Badan" -> Brush.linearGradient(listOf(Color(0xFF4ECDC4), Color(0xFF44A08D)))
    "Usia" -> Brush.linearGradient(listOf(Color(0xFFFF7675), Color(0xFFFD79A8)))
    "BMI" -> Brush.linearGradient(listOf(Color(0xFFFFECD2), Color(0xFFFCB69F)))
    else -> Brush.linearGradient(listOf(Color.Gray, Color.DarkGray))
}

@Composable
fun InfoCard(icon: String, value: String, label: String, onClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Color.Black.copy(alpha = 0.04f),
                spotColor = Color.Black.copy(alpha = 0.04f),
                clip = false
            )
            .background(Color.White, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .size(120.dp) // ukuran tetap agar mudah centering
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(accentColorFor(label), RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
        )

        // Kolom di tengah-tengah Box, pakai Modifier.align untuk center
        Column(
            modifier = Modifier
                .align(Alignment.Center)  // ini penting untuk vertikal & horizontal center
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(accentColorFor(label), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(icon, fontSize = 20.sp, color = Color.White)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color(0xFF2C3E50))
            Text(label, fontSize = 14.sp, color = Color(0xFF7F8C8D))
        }
    }
}


data class MenuItemData(val icon: String, val title: String, val subtitle: String, val onClick: () -> Unit)

@Composable
fun MenuSection(title: String, items: List<MenuItemData>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = Color.Black.copy(alpha = 0.04f),
                spotColor = Color.Black.copy(alpha = 0.04f),
                clip = false
            )
            .background(Color.White, RoundedCornerShape(20.dp))
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 8.dp),
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = Color(0xFF2C3E50)
        )

        items.forEachIndexed { index, item ->
            MenuItem(item)
            if (index < items.size - 1) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    thickness = 1.dp,
                    color = Color(0xFFF8F9FA)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}


fun menuIconColor(title: String): Brush = when (title) {
    "Profil Kesehatan" -> Brush.linearGradient(listOf(Color(0xFFFF7675), Color(0xFFFD79A8)))
    "Target & Tujuan" -> Brush.linearGradient(listOf(Color(0xFF4ECDC4), Color(0xFF44A08D)))
    "Pusat Bantuan" -> Brush.linearGradient(listOf(Color(0xFF89F7FE), Color(0xFF66A6FF)))
    "Tentang YourDietBuddy" -> Brush.linearGradient(listOf(Color(0xFFD299C2), Color(0xFFEFE9D7)))
    "Keluar" -> Brush.linearGradient(listOf(Color(0xFFFF9A9E), Color(0xFFFECFEF)))
    else -> Brush.linearGradient(listOf(Color.Gray, Color.DarkGray))
}


@Composable
fun MenuItem(item: MenuItemData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { item.onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(menuIconColor(item.title), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(item.icon, color = Color.White, fontSize = 18.sp)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(item.title, fontWeight = FontWeight.Medium, fontSize = 16.sp, color = Color(0xFF2C3E50))
            Text(item.subtitle, fontSize = 12.sp, color = Color.Gray)
        }
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.Gray)
    }
}

@Composable
fun HealthProfileSection(
    initialData: Map<String, String>,
    onSave: (Map<String, String>) -> Unit,
    onCancel: () -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    val formData = remember { mutableStateMapOf<String, String>().apply { putAll(initialData) } }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Profil Kesehatan", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        if (isEditing) {
            listOf("Kondisi Medis", "Alergi", "Diet Khusus").forEach { field ->
                OutlinedTextField(
                    value = formData[field] ?: "",
                    onValueChange = { formData[field] = it },
                    label = { Text(field) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = {
                    formData.clear()
                    formData.putAll(initialData)
                    isEditing = false
                    onCancel()
                }) {
                    Text("Batal")
                }
                Button(onClick = {
                    onSave(formData.toMap())
                    isEditing = false
                }) {
                    Text("Selesai")
                }
            }
        } else {
            listOf("Kondisi Medis", "Alergi", "Diet Khusus").forEach { field ->
                Text("$field: ${formData[field].orEmpty()}", modifier = Modifier.padding(vertical = 6.dp))
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Button(onClick = { isEditing = true }) {
                    Text("Edit")
                }
            }
        }
    }
}

@Composable
fun GoalTargetSection(
    initialData: Map<String, String>,
    onSave: (Map<String, String>) -> Unit,
    onCancel: () -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    val formData = remember { mutableStateMapOf<String, String>().apply { putAll(initialData) } }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Target & Tujuan", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        if (isEditing) {
            listOf("Kalori Harian", "Berat Ideal", "Timeline").forEach { field ->
                OutlinedTextField(
                    value = formData[field] ?: "",
                    onValueChange = { formData[field] = it },
                    label = { Text(field) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = {
                    formData.clear()
                    formData.putAll(initialData)
                    isEditing = false
                    onCancel()
                }) {
                    Text("Batal")
                }
                Button(onClick = {
                    onSave(formData.toMap())
                    isEditing = false
                }) {
                    Text("Selesai")
                }
            }
        } else {
            listOf("Kalori Harian", "Berat Ideal", "Timeline").forEach { field ->
                Text("$field: ${formData[field].orEmpty()}", modifier = Modifier.padding(vertical = 6.dp))
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Button(onClick = { isEditing = true }) {
                    Text("Edit")
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(navController = rememberNavController())
}