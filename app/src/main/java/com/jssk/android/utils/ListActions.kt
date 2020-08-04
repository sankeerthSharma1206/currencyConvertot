package com.jssk.android.utils

import com.jssk.android.interfaces.ListActionsInterface

open class ListActions: ListActionsInterface
{
    override fun onItemClicked(pos: Int) {}

    override fun onRoleClicked(pos: Int) {}

    override fun onEditClicked(pos: Int) {}

    override fun onStatusClicked(pos: Int) {}

    override fun onRemoveClicked(pos: Int) {}

    override fun onAddClicked(pos: Int) {}

    override fun loadMoreItems() {}

    override fun loadingCompleted() {}

    override fun onClaimClicked(pos: Int) {}

    override fun onBuyClicked(pos: Int) {}

    override fun onItemSelected(pos: Int) {}
}