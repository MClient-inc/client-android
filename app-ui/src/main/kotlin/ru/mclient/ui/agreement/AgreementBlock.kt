package ru.mclient.ui.agreement

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mclient.ui.utils.defaultPlaceholder
import ru.mclient.ui.view.HtmlText

class AgreementBlockState(
    val title: String?,
    val content: String?,
)

@Composable
fun AgreementBlock(
    state: AgreementBlockState,
    modifier: Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Crossfade(targetState = state.title) { content ->
            Column(modifier = Modifier.fillMaxWidth()) {
                when (content) {
                    null ->
                        Text(
                            text = "Соглашение",
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .defaultPlaceholder(visible = true)
                                .align(CenterHorizontally),
                            style = MaterialTheme.typography.titleLarge,
                        )

                    else ->
                        Text(
                            text = content,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .align(CenterHorizontally),
                            style = MaterialTheme.typography.titleLarge,
                        )
                }
            }
        }
        Crossfade(targetState = state.content, modifier = Modifier.padding(10.dp)) { content ->
            when (content) {
                null ->
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .defaultPlaceholder()
                    )

                else -> HtmlText(
                    content = content,
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                )
            }
        }
    }
}

@Preview
@Composable
fun AgreementBlockPreview() {
    AgreementBlock(
        state = AgreementBlockState(
            title = "Пользовательское соглашение MClients",
            content = """«Система MClients» (далее MClients) предоставляет свои функциональные возможности на платной основе. Переходя к использованию MClients, вы соглашаетесь с изложенными ниже правилами Пользовательского соглашения.

**1. Общие правила**

1.1. Настоящее Пользовательское соглашение (далее – Соглашение) определяет общие условия использования программного обеспечения MClients в мобильном приложении MClients (далее – MClients), а также права и обязанности Пользователя и Лицензиара. Настоящее Соглашение распространяется также на отношения, связанные с правами и интересами третьих лиц, не являющимися Пользователями MClients, но чьи права и интересы могут быть затронуты в результате действий Пользователей MClients.

1.2. Настоящее Соглашение составляет соглашение между Пользователем и Лицензиаром относительно порядка использования MClients и ее сервисов и заменяют собой все предыдущие соглашения, которые имели место или могли иметь место между Пользователем и Лицензиаром вне зависимости от формы, в которой таковые соглашения были достигнуты.

1.3. Настоящее Соглашение является юридически обязательным соглашением между Пользователем и Лицензиаром, предметом которого является предоставление Лицензиаром Пользователю права использования на условиях простой (неисключительной) лицензии MClients в соответствии с ее функциональными возможностями, в объеме, определенном Лицензиаром, а также оказания иных сопутствующих услуг и предоставления иных сервисов (далее – Услуги). Помимо настоящего Соглашения, к соглашению между Пользователем и Лицензиаром относятся все прочие документы, регулирующие предоставление Услуг и размещенные в соответствующих разделах MClients.

1.4. Пользователь обязан полностью ознакомиться с настоящим Соглашением до момента регистрации в MClients. Регистрация Пользователя в MClients означает полное и безоговорочное принятие Пользователем настоящего Пользовательского соглашения в соответствии со статьей 438 Гражданского кодекса Российской Федерации.

1.5. Настоящее Соглашение может быть изменено и (или) дополнено Лицензиаром в одностороннем порядке без какого-либо специального уведомления Пользователя. Настоящее Соглашение является открытым и общедоступным документом.

1.6. Пользователь обязан регулярно проверять условия настоящего Соглашения на предмет их изменения и (или) дополнения. Продолжение использования MClients Пользователем после внесения изменений и (или) дополнений в настоящее Соглашение означает принятие и согласие Пользователя со всеми такими изменениями и (или) дополнениями.

1.7. Никакие положения настоящего Соглашения не предоставляют Пользователю прав на использование фирменного наименования, товарных знаков, доменных имен и результатов интеллектуальной деятельности, используемых в MClients, за исключением случаев, когда такое использование допускается с письменного и предварительного согласия Лицензиара.

1.8. При регистрации Пользователь соглашается с настоящим Соглашением и принимает на себя указанные в нем права и обязанности, связанные с использованием и функционированием MClients.

1.9. Принимая настоящее Соглашение, Пользователь подтверждает свое согласие на обработку Лицензиаром персональных данных Пользователя, предоставленных Пользователем при регистрации, а также размещаемых Пользователем добровольно в MClients.

1.10. Пользователь вправе заключить с Лицензиаром отдельный Лицензионный договор на использование MClients без ограничений, установленных для использования MClients в бесплатной версии.

""",
        ), modifier = Modifier.fillMaxSize()
    )
}