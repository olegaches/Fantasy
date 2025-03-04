
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Fantasy World",
        //undecorated = true, // Убираем рамки окна
        state = rememberWindowState(placement = WindowPlacement.Maximized) // Полноэкранный режим
    ) {
        MaterialTheme {
            ImageGrid()
        }
    }
}

data class Item(
    val image: String,
    val startImage: String = "podsneg.jpg",
    val texts: List<String>,
    val borderColor: Color,
)

@Composable
fun ImageGrid() {
    var activeImage by remember { mutableStateOf<Int?>(null) }
    val items = rememberSaveable {
        listOf(
            Item(
                image = "lady_bug.jpg",
                startImage = "podsneg.jpg",
                texts = listOf(
                    "Привет, мои дорогие! Я - Леди Баг! Давайте покажем нашим мамочкам весёлый танец!",
                    "СПАСИБО!"
                ),
                borderColor = Color.White,
            ),
            Item(
                image = "karamelka.png",
                startImage = "mimosa.jpg",
                texts = listOf(
                    "Привет всем! Да, это я!",
                    "Спасибо, теперь я буду послушной!"
                ),
                borderColor = Color.White,
            ),
            Item(
                image = "potter.jpg",
                startImage = "landish.jpeg",
                texts = listOf(
                    "Я - Гарри Поттер, и предлагаю вам испытание \"Передай любовь \". ",
                    "Вы правы, семья имеет волшебную силу!\n"
                ),
                borderColor = Color.White,
            ),
            Item(
                image = "masha.jpeg",
                startImage = "tulpan.jpg",
                texts = listOf(
                    "Привет, ребята! Мы просим вас исполнить песню о Весне!",
                    "Какая красивая песня. Спасибо!"
                ),
                borderColor = Color.White,
            ),
            Item(
                image = "baba_yaga.jpeg",
                startImage = "narcis.jpg",
                texts = listOf(
                    "Здравствуйте, касатики! Спойте песню для бабушки.",
                    "Спасибо ребятки, повеселили старушку."
                ),
                borderColor = Color.White,
            ),
            Item(
                image = "zolushka.jpg",
                startImage = "giacint.jpg",
                texts = listOf(
                    "Здравствуйте, друзья. Спойте, пожалуйста, песню  о послушной дочке.\n",
                    "Ах, какие трудолюбивые девочки!"
                ),
                borderColor = Color.White,
            ),
        )
    }
    Image(
        modifier = Modifier.fillMaxSize(),
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        painter = painterResource("try_svg.svg")
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(3.dp)
    ) {
        for (row in 0 until 2) {
            Row(modifier = Modifier.weight(1f)) {
                for (col in 0 until 3) {
                    val index = row * 3 + col
                    val item = items[index]
                    ImageItem(
                        image = item.image,
                        startImage = item.startImage,
                        isActive = activeImage == index || activeImage == null,
                        onChangeActive = {
                            if(it) {
                                activeImage = index
                            } else {
                                activeImage = null
                            }
                        },
                        texts = item.texts,
                        borderColor = item.borderColor,
                    )
                }
            }
        }
    }
}

@Composable
fun RowScope.ImageItem(
    image: String,
    startImage: String,
    isActive: Boolean,
    onChangeActive: (Boolean) -> Unit,
    texts: List<String>,
    borderColor: Color,
) {
    var clickCount by rememberSaveable { mutableStateOf(0) }
    var scale by rememberSaveable { mutableStateOf(1f) }
    val scaleAnim = animateFloatAsState(
        targetValue = if (scale <= 1f) 1f else 1.1f,
        animationSpec = tween(200),
        finishedListener = { scale = 1f }
    )
    var rotated by rememberSaveable { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (clickCount == 3)
            90f
        else if(rotated) {
            180f
        } else {
            0f
        },
        animationSpec = tween(500), label = ""
    )
    val alpha by animateFloatAsState(
        targetValue = if (clickCount != 3) 1f else 0f,
        animationSpec = tween(durationMillis = 6000, easing = FastOutSlowInEasing),
        label = "alpha"
    )
    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .background(Color.White.copy(alpha = alpha))
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .scale(scaleAnim.value)
                .graphicsLayer {
                    rotationY = rotation
                    cameraDistance = 9 * density
                }
                .clickable(isActive && clickCount != 3) {
                    scale = 1.2f
                    if(clickCount == 3) {
                        clickCount = 0
                    } else {
                        clickCount++
                    }
                    if(clickCount == 1) {
                        rotated = !rotated
                    }
                    if(clickCount == 3) {
                        rotated = !rotated
                    }
                    onChangeActive(clickCount != 3)
                }
                .border(color = borderColor, width = 3.dp)
        ) {
            if(rotation >= 90f) {
                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            rotationY = 180f
                        }
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxSize(),
                        painter = painterResource(image),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds
                    )
                    AnimatedBubble(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .padding(horizontal = 4.dp),
                        clickCount = clickCount,
                        texts = texts,
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxSize(),
                        painter = painterResource(startImage),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds
                    )
                }
            }
        }
    }
}

@Composable
fun AnimatedBubble(
    modifier: Modifier,
    clickCount: Int,
    texts: List<String>,
) {
    SpeechBubble(
        modifier = modifier,
        text = texts.getOrElse(clickCount - 1) { texts.last() },
    )
//    AnimatedContent(
//        modifier = modifier,
//        transitionSpec = {
//            ContentTransform(
//                targetContentEnter = fadeIn(animationSpec = tween(2000)),
//                initialContentExit = fadeOut(animationSpec = tween(2000)),
//                sizeTransform = SizeTransform(sizeAnimationSpec = { _, _ -> tween(2000) })
//            )
//        },
//        targetState = clickCount,
//    ) {
//        SpeechBubble(
//            modifier = Modifier,
//            text = texts.getOrElse(clickCount - 1) { texts.last() },
//        )
//    }
}

@Composable
fun SpeechBubble(text: String, modifier: Modifier) {
    Box(modifier) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Column {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .border(
                            width = 1.dp,
                            color = Color.Black,
                            shape = CircleShape,
                        )
                        .clip(CircleShape)
                        .background(Color.White)
                )
                Spacer(modifier = Modifier.height(5.dp))
                Box(
                    modifier = Modifier
                        .size(15.dp)
                        .border(
                            width = 1.dp,
                            color = Color.Black,
                            shape = CircleShape,
                        )
                        .clip(CircleShape)
                        .background(Color.White)
                )
                Spacer(modifier = Modifier.height(5.dp))
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .border(
                            width = 1.dp,
                            color = Color.Black,
                            shape = CircleShape,
                        )
                        .clip(CircleShape)
                        .background(Color.White)
                )
                Spacer(modifier = Modifier.height(5.dp))
                Box(
                    modifier = Modifier
                        .size(25.dp)
                        .border(
                            width = 1.dp,
                            color = Color.Black,
                            shape = CircleShape,
                        )
                        .clip(CircleShape)
                        .background(Color.White)
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 2.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(50.dp),
                    )
                    .clip(RoundedCornerShape(50.dp))
                    .animateContentSize(),
                color = Color.White,
            ) {
                Box(modifier = Modifier.padding(22.dp)) {
                    Text(
                        text = text,
                        lineHeight = 30.sp,
                        color = Color.Black,
                        fontSize = 35.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}
