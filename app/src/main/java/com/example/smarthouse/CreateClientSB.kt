package com.example.smarthouse

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

class CreateClientSB {
    val clientSB = createSupabaseClient(
        supabaseUrl = "https://chxgpselqrvimubuzglm.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImNoeGdwc2VscXJ2aW11YnV6Z2xtIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MDA4MDg0MjIsImV4cCI6MjAxNjM4NDQyMn0.VBHt9YIRnUheWFZWcNbLCL1wfK5sVJXenc_jHPEw46M"
//        supabaseUrl = "https://msrymwiawamltodzgdyr.supabase.co",
//        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im1zcnltd2lhd2FtbHRvZHpnZHlyIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MDA2NDg2NDYsImV4cCI6MjAxNjIyNDY0Nn0.3PrDR_z3J8nWHIrzlBjhRuJmN235jgDydjUX8Xlj01s"
    ) {
        install(GoTrue)
        install(Postgrest)
        install(Storage)
    }
}