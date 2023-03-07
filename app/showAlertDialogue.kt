ackage drawable

fun showAlertDialogue() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Root Retected")
            .setMessage("This app cannot run on a rooted device.")
            .setNegativeButton("Okay") { dialogInterface: DialogInterface, i: Int ->
                exitProcess(0)
            }
    }