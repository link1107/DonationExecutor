import igorlink.donationexecutor.executionsstaff.Donation
import net.kyori.adventure.text.Component
import org.bukkit.Server
import org.bukkit.command.CommandSender
import org.bukkit.permissions.Permission
import org.bukkit.permissions.PermissionAttachment
import org.bukkit.permissions.PermissionAttachmentInfo
import org.bukkit.plugin.Plugin
import org.junit.jupiter.api.Test
import java.util.*



internal class TestGetterDonation {
    private val donation: Donation = Donation(testSender,"IgorLink", 100.toString(), "test Getter")

    @Test
    fun testGetterDonation() {
        assert(donation.message == "test Getter")
        assert(donation.amount == "100")
        assert(donation.message == "test Getter")
        assert(donation.executionName == null)
    }

    @Test
    fun testSetterExecutionName() {
        donation.executionName = "testString"
        assert(donation.executionName == "testString")
    }
}

private val testSender: CommandSender = object: CommandSender{
    override fun spigot(): CommandSender.Spigot {
        TODO("Not yet implemented")
    }

    override fun name(): Component {
        TODO("Not yet implemented")
    }

    override fun sendMessage(message: String) {
        TODO("Not yet implemented")
    }

    override fun sendMessage(vararg messages: String) {
        TODO("Not yet implemented")
    }

    override fun sendMessage(sender: UUID?, message: String) {
        TODO("Not yet implemented")
    }

    override fun sendMessage(sender: UUID?, vararg messages: String) {
        TODO("Not yet implemented")
    }

    override fun isOp(): Boolean {
        TODO("Not yet implemented")
    }

    override fun setOp(value: Boolean) {
        TODO("Not yet implemented")
    }

    override fun isPermissionSet(name: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun isPermissionSet(perm: Permission): Boolean {
        TODO("Not yet implemented")
    }

    override fun hasPermission(name: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun hasPermission(perm: Permission): Boolean {
        TODO("Not yet implemented")
    }

    override fun addAttachment(plugin: Plugin, name: String, value: Boolean): PermissionAttachment {
        TODO("Not yet implemented")
    }

    override fun addAttachment(plugin: Plugin): PermissionAttachment {
        TODO("Not yet implemented")
    }

    override fun addAttachment(plugin: Plugin, name: String, value: Boolean, ticks: Int): PermissionAttachment? {
        TODO("Not yet implemented")
    }

    override fun addAttachment(plugin: Plugin, ticks: Int): PermissionAttachment? {
        TODO("Not yet implemented")
    }

    override fun removeAttachment(attachment: PermissionAttachment) {
        TODO("Not yet implemented")
    }

    override fun recalculatePermissions() {
        TODO("Not yet implemented")
    }

    override fun getEffectivePermissions(): MutableSet<PermissionAttachmentInfo> {
        TODO("Not yet implemented")
    }

    override fun getServer(): Server {
        TODO("Not yet implemented")
    }

    override fun getName(): String {
        TODO("Not yet implemented")
    }
}
