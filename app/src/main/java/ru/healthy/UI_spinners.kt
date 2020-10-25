package ru.healthy

import androidx.compose.foundation.*
import androidx.compose.foundation.Box
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@androidx.compose.runtime.Composable
fun myTopDropDownMenu(model: AppViewModel) {
    var expanded by state { false }
    DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            toggle = {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.MoreVert)
                }
            },
            toggleModifier = Modifier.wrapContentSize(Alignment.TopEnd)
    ) {
        DropdownMenuItem(onClick = {
            model.isAdmin = true
        }) {
            Text("SearchTop10")
        }
        //Divider()
    }

}

@androidx.compose.runtime.Composable
fun myDistrictSpinner(model: AppViewModel, user: Map<String, String>, tstyle: TextStyle = TextStyle()) {
    var expanded by state { false }
    var spinnerText by state { "${user["R"]}" }
    val list = model.districtList
    val iconButton = @androidx.compose.runtime.Composable {
        Row(modifier = Modifier.border(1.dp, Color.LightGray).preferredWidth(280.dp)) {
            Spacer(Modifier.preferredWidth(padd))
            Text(
                    "$spinnerText",
                    modifier = Modifier.gravity(Alignment.CenterVertically),
                    style = tstyle
            )
            IconButton(onClick = { expanded = true })
            {
                Icon(Icons.Default.ArrowDropDown)
            }
        }
    }
    Row {
        DropdownMenu(
            dropdownModifier = mod_back(),
                    expanded = expanded,
            onDismissRequest = { expanded = false },
            toggle = iconButton
        ) {
            Column {
                list.value?.forEach {
                    DropdownMenuItem(
                            enabled = true,
                            onClick = { expanded = false; },
                            content = {
                                Box(
                                    modifier = Modifier.clickable(onClick = {
                                        spinnerText = "${it["Name"]}"
                                        expanded = false
                                        var usr = model.current_usr.toMutableMap()
                                        usr["R"] = spinnerText
                                        usr["iR"] = "${it["IdDistrict"]}"
                                        model.current_usr = usr
                                        if (model.isAdmin) model.readLpuList()
                                    })
                                ) {
                                    Text("${it["Name"]}")
                                }
                            }
                    )
                }
            }
        }
    }

}

@androidx.compose.runtime.Composable
fun myLPUSpinner(model: AppViewModel, user: Map<String, String>, tstyle: TextStyle = TextStyle()) {
    var expanded by state { false }
    var spinnerText by state { "${user["L"]}" }
    val list = model.lpuList
    val iconButton = @androidx.compose.runtime.Composable {
        Row(modifier = Modifier.drawBorder(1.dp, Color.LightGray).preferredWidth(280.dp)) {
            Spacer(Modifier.preferredWidth(padd))
            Text(
                    "$spinnerText",
                    modifier = Modifier.gravity(Alignment.CenterVertically),
                    style = tstyle
            )
            IconButton(onClick = { expanded = true })
            {
                Icon(Icons.Default.ArrowDropDown)
            }
        }
    }
    Row {
        DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                toggle = iconButton
        ) {
            Column {
                list.value?.forEach {
                    DropdownMenuItem(
                            enabled = true,
                            onClick = { expanded = false; },
                            content = {
                                Box(
                                        modifier = Modifier.clickable(onClick = {
                                            spinnerText = "${it["Name"]}"
                                            expanded = false
                                            var usr = model.current_usr.toMutableMap()
                                            usr["L"] = spinnerText
                                            usr["iL"] = "${it["IdLPU"]}"
                                            model.current_usr = usr

                                        })
                                ) {
                                    Text("${it["Name"]} (${it["Description"]})")
                                }
                            }
                    )
                }
            }
        }
    }

}
