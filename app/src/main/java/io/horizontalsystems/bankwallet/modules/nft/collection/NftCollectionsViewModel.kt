package io.horizontalsystems.bankwallet.modules.nft.collection

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.horizontalsystems.bankwallet.entities.CurrencyValue
import io.horizontalsystems.bankwallet.entities.DataState
import io.horizontalsystems.bankwallet.entities.ViewState
import io.horizontalsystems.bankwallet.modules.nft.NftCollectionRecord
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class NftCollectionsViewModel(private val service: NftCollectionsService) : ViewModel() {
    val priceType by service::priceType

    var viewState by mutableStateOf<ViewState?>(null)
        private set
    var loading by mutableStateOf(false)
        private set

    var collectionViewItems by mutableStateOf<List<NftCollectionViewItem>>(listOf())
        private set
    var totalCurrencyPrice by mutableStateOf<CurrencyValue?>(null)
        private set

    init {
        viewModelScope.launch {
            service.assetItemsPriced
                .collect {
                    handleNftCollections(it)
                }
        }

        service.start()
    }

    private fun handleNftCollections(nftCollectionsState: DataState<Pair<Map<NftCollectionRecord, List<NftAssetItemPricedWithCurrency>>, CurrencyValue>>) {
        loading = nftCollectionsState.loading

        nftCollectionsState.dataOrNull?.let {
            viewState = ViewState.Success

            syncItems(it.first, it.second)
        }
    }

    private fun syncItems(
        collectionItems: Map<NftCollectionRecord, List<NftAssetItemPricedWithCurrency>>,
        totalCurrencyPrice: CurrencyValue
    ) {
        val expandedStates = collectionViewItems.associate { it.slug to it.expanded }
        collectionViewItems = collectionItems.map { (collectionRecord, assetItems) ->
            NftCollectionViewItem(
                slug = collectionRecord.slug,
                name = collectionRecord.name,
                imageUrl = collectionRecord.imageUrl,
                assets = assetItems,
                expanded = expandedStates[collectionRecord.slug] ?: false
            )
        }

        this.totalCurrencyPrice = totalCurrencyPrice
    }

    override fun onCleared() {
        service.stop()
    }

    fun refresh() {
        viewModelScope.launch {
            loading = true
            service.refresh()
            loading = false
        }
    }

    fun toggleCollection(collection: NftCollectionViewItem) {
        val index = collectionViewItems.indexOf(collection)

        if (index != -1) {
            collectionViewItems = collectionViewItems.toMutableList().apply {
                this[index] = collection.copy(expanded = !collection.expanded)
            }
        }
    }

    fun updatePriceType(priceType: PriceType) {
        service.updatePriceType(priceType)
    }

}
