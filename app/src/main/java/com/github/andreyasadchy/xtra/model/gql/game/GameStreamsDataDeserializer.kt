package com.github.andreyasadchy.xtra.model.gql.game

import com.github.andreyasadchy.xtra.model.helix.stream.Stream
import com.github.andreyasadchy.xtra.model.helix.tag.Tag
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type

class GameStreamsDataDeserializer : JsonDeserializer<GameStreamsDataResponse> {

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): GameStreamsDataResponse {
        val data = mutableListOf<Stream>()
        val dataJson = json.asJsonObject?.getAsJsonObject("data")?.getAsJsonObject("game")?.getAsJsonObject("streams")?.getAsJsonArray("edges")
        val cursor = dataJson?.lastOrNull()?.asJsonObject?.get("cursor")?.takeIf { !it.isJsonNull }?.asString
        dataJson?.forEach { item ->
            item?.asJsonObject?.getAsJsonObject("node")?.let { obj ->
                val tags = mutableListOf<Tag>()
                obj.get("tags")?.takeIf { it.isJsonArray }?.asJsonArray?.forEach { tagElement ->
                    tagElement?.takeIf { it.isJsonObject }?.asJsonObject.let { tag ->
                        tags.add(Tag(
                            id = tag?.get("id")?.takeIf { !it.isJsonNull }?.asString,
                            name = tag?.get("localizedName")?.takeIf { !it.isJsonNull }?.asString,
                        ))
                    }
                }
                data.add(Stream(
                    id = obj.get("id")?.takeIf { !it.isJsonNull }?.asString,
                    user_id = obj.get("broadcaster")?.takeIf { it.isJsonObject }?.asJsonObject?.get("id")?.takeIf { !it.isJsonNull }?.asString,
                    user_login = obj.get("broadcaster")?.takeIf { it.isJsonObject }?.asJsonObject?.get("login")?.takeIf { !it.isJsonNull }?.asString,
                    user_name = obj.get("broadcaster")?.takeIf { it.isJsonObject }?.asJsonObject?.get("displayName")?.takeIf { !it.isJsonNull }?.asString,
                    type = obj.get("type")?.takeIf { !it.isJsonNull }?.asString,
                    title = obj.get("title")?.takeIf { !it.isJsonNull }?.asString,
                    viewer_count = obj.get("viewersCount")?.takeIf { !it.isJsonNull }?.asInt,
                    thumbnail_url = obj.get("previewImageURL")?.takeIf { !it.isJsonNull }?.asString,
                    profileImageURL = obj.get("broadcaster")?.takeIf { it.isJsonObject }?.asJsonObject?.get("profileImageURL")?.takeIf { !it.isJsonNull }?.asString,
                    tags = tags
                ))
            }
        }
        return GameStreamsDataResponse(data, cursor)
    }
}
