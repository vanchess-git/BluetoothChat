package com.karoliinamultas.bluetoothchat.data

import kotlinx.coroutines.flow.Flow

class OfflineMessagesRepository(private val messageDao: MessageDao) : MessagesRepository {

    override suspend fun insertMessage(message: Message) = messageDao.insert(message)

    override suspend fun updateMessage(message: Message) = messageDao.update(message)

    override suspend fun deleteMessage(message: Message) = messageDao.delete(message)

    override fun getChatMessages(chatId: String): Flow<List<Message>> = messageDao.getChatMessages(chatId)

    override suspend fun deleteAllChatMessages() = messageDao.deleteAllChatMessages()

    override suspend fun deleteSingleChatMessages(chatId: String) = messageDao.deleteSingleChatMessages(chatId)
}