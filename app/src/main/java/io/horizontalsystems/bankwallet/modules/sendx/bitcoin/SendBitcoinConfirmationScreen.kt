package io.horizontalsystems.bankwallet.modules.sendx.bitcoin

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import io.horizontalsystems.bankwallet.modules.amount.AmountInputModeViewModel
import io.horizontalsystems.bankwallet.modules.sendx.SendConfirmationScreen
import io.horizontalsystems.bankwallet.modules.xrate.XRateViewModel

@Composable
fun SendBitcoinConfirmationScreen(
    navController: NavController,
    sendViewModel: SendBitcoinViewModel,
    xRateViewModel: XRateViewModel,
    amountInputModeViewModel: AmountInputModeViewModel
) {
    val confirmationData = sendViewModel.getConfirmationData()

    SendConfirmationScreen(
        navController = navController,
        coinMaxAllowedDecimals = sendViewModel.coinMaxAllowedDecimals,
        feeCoinMaxAllowedDecimals = sendViewModel.coinMaxAllowedDecimals,
        fiatMaxAllowedDecimals = sendViewModel.fiatMaxAllowedDecimals,
        amountInputType = amountInputModeViewModel.inputType,
        rate = xRateViewModel.rate,
        feeCoinRate = xRateViewModel.rate,
        sendResult = sendViewModel.sendResult,
        coin = confirmationData.coin,
        feeCoin = confirmationData.coin,
        amount = confirmationData.amount,
        address = confirmationData.address,
        fee = confirmationData.fee,
        lockTimeInterval = confirmationData.lockTimeInterval,
        onClickSend = sendViewModel::onClickSend
    )
}