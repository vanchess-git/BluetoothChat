package com.karoliinamultas.bluetoothchat.ui.chat


import android.media.Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.karoliinamultas.bluetoothchat.*
import com.karoliinamultas.bluetoothchat.R
import kotlinx.coroutines.launch
import java.net.URL


private const val TAG = "ChatCompose"


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ChatWindow(navController: NavController){
    //Statusbar
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(MaterialTheme.colorScheme.background)
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground
                ),
                title = {
                    Text(
                        "Restroom Chat",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screen.ShowChats.route) }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back button"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Menu button"
                        )
                    }
                },
            )
        },
        content = { innerPadding ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)) {
                Chats()
            }
        }
    )
}


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ShowChat(modifier: Modifier = Modifier) {

        val tekstit = listOf(
            Color(0xFF00FDDC),
            Color(0xFFFF729F),
            Color(0xFF04E762),
            Color(0xFFFDE74C),
            Color(0xFFFF4365))
        val randomTeksti = tekstit.random()

        val backgroundit = listOf(
            Color(0xFF111D4A),
            Color(0xFF43AA8B),
            Color(0xFF8B635C),
            Color(0xFF60594D),
            Color(0xFF93A29B))
        val randomBack = backgroundit.random()

        Row(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .width(150.dp)
                    .padding(5.dp),

                colors = CardDefaults.cardColors(containerColor = randomBack),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Text(text = "Chat box", color = randomTeksti, modifier = Modifier.padding(10.dp))
            }
        }
    }


@Composable
    fun Chats(/*deviceName: String?*/ modifier: Modifier = Modifier) {

        val inputvalue = remember { mutableStateOf(TextFieldValue()) }


    Column(modifier = Modifier.fillMaxSize()) {

        Surface(modifier = Modifier
            .padding(all = Dp(5f))
            .fillMaxHeight(fraction = 0.89f)) {
            ChatsList()
        }
        InputField()
    }
        }

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
    @Composable
    fun InputField(/*inputvalue: MutableState<TextFieldValue>*/ modifier: Modifier = Modifier) {
        val focusManager = LocalFocusManager.current
        val context = LocalContext.current
        //BotMenu
        // Declaring a Boolean value to
        // store bottom sheet collapsed state
        val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(bottomSheetState =
        BottomSheetState(BottomSheetValue.Collapsed))

        // Declaring Coroutine scope
        val coroutineScope = rememberCoroutineScope()

        BottomSheetScaffold(
            scaffoldState = bottomSheetScaffoldState,
            sheetContent =  {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f))) {
                    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.End) {

                            IconButton(
                                onClick = {
                                    coroutineScope.launch {
                                        if (bottomSheetScaffoldState.bottomSheetState.isExpanded) {
                                            bottomSheetScaffoldState.bottomSheetState.collapse()
                                        } else {
                                            bottomSheetScaffoldState.bottomSheetState.expand()
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .height(60.dp)
                                    .width(60.dp)
                                    .padding(0.dp, 6.dp, 0.dp, 0.dp),
                                colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.background),
                                content = {
                                    Icon(
                                        imageVector = Icons.Filled.KeyboardArrowDown,
                                        contentDescription = "Localized description"
                                    )
                                }
                            )}
                    Column(
                        Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row() {
                            IconButton(
                                onClick = { },
                                modifier = Modifier
                                    .height(60.dp)
                                    .width(60.dp)
                                    .padding(0.dp, 6.dp, 0.dp, 0.dp),
                                colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.background),
                                content = {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = R.drawable.draw),
                                        contentDescription = "Localized description"
                                    )
                                }
                            )
                            CameraButton(context)
                            GalleryButton(context)
                        }
                    }
                }
            },
            sheetPeekHeight = 0.dp
        ){
        Box(
            Modifier
                .background(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 1f))) {
            Row(
                Modifier
                    .padding(5.dp)
            ) {
                TextField(
                    value = "",
                    onValueChange = {
                        ""
                    },
                    Modifier
                        .width(265.dp)
                        .padding(5.dp),
                    shape = RoundedCornerShape(5.dp),
                    placeholder = { Text(text = "Enter your message") },
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.None,
                        autoCorrect = true,
                        keyboardType = KeyboardType.Text,
                        imeAction = androidx.compose.ui.text.input.ImeAction.Done,
                    ),
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = TextUnit.Unspecified,
                        fontFamily = FontFamily.SansSerif
                    ),
                    maxLines = 1,
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(containerColor = MaterialTheme.colorScheme.background)
                )

                IconButton(
                    onClick = { /*Sending message comes here*/ },
                    modifier = Modifier
                        .height(60.dp)
                        .width(60.dp)
                        .padding(0.dp, 6.dp, 0.dp, 0.dp),
                    colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.background),
                    content = {
                        Icon(
                            imageVector = Icons.Filled.Send,
                            contentDescription = "Localized description"
                        )
                    }
                )

                IconButton(
                    onClick = { coroutineScope.launch {
                        if (bottomSheetScaffoldState.bottomSheetState.isCollapsed){
                            bottomSheetScaffoldState.bottomSheetState.expand()
                        }else{
                            bottomSheetScaffoldState.bottomSheetState.collapse()
                        }
                    } },
                    modifier = Modifier
                        .height(60.dp)
                        .width(60.dp)
                        .padding(0.dp, 6.dp, 0.dp, 0.dp),
                    colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.background),
                    content = {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowUp,
                            contentDescription = "Localized description"
                        )
                    }
                )

                //CameraButton(context)

            }}
        }
    }

    @Composable
    fun ChatsList(/*messagesList: List<Message>*/ modifier: Modifier = Modifier) {
        LazyColumn(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            items(/*count = messagesList.size*/1) { index ->
                    ShowChat()
            }
        }
    }