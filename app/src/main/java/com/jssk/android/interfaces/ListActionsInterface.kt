package com.jssk.android.interfaces

interface ListActionsInterface
{
    fun onItemClicked(pos: Int)
    fun onEditClicked(pos: Int)
    fun onRoleClicked(pos: Int)
    fun onStatusClicked(pos: Int)
    fun onRemoveClicked(pos: Int)
    fun onAddClicked(pos: Int)
    fun loadMoreItems()
    fun loadingCompleted()
    fun onClaimClicked(pos: Int)
    fun onBuyClicked(pos: Int)
    fun onItemSelected(pos: Int)
}