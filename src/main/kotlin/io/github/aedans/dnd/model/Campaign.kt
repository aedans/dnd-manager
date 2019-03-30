package io.github.aedans.dnd.model

import io.github.aedans.dnd.controller.Gson
import io.github.aedans.dnd.controller.Util
import io.reactivex.Observable
import java.io.File

data class Campaign(val name: String, val settingName: String) {
    companion object {
        fun read(campaignName: String) =
            Gson.read<Campaign>(File(Util.campaignsFile, "$campaignName.json"))

        fun write(campaign: Campaign) =
            Gson.write(campaign, File(Util.campaignsFile, "${campaign.name}.json"))

        fun delete(campaignName: String) =
            File(Util.campaignsFile, "$campaignName.json").delete()

        fun list(): Observable<Campaign> {
            val sequence = Util.campaignsFile.listFiles().asSequence()
            return Observable.fromIterable(sequence.asIterable())
                .map { Campaign.read(it.nameWithoutExtension) }
        }
    }
}