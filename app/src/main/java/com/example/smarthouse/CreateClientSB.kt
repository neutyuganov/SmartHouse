package com.example.smarthouse

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.postgrest.Postgrest

class CreateClientSB {
    val clientSB = createSupabaseClient(
//        supabaseUrl = "https://lurgsovrpjaixkmrkint.supabase.co",
//        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imx1cmdzb3ZycGphaXhrbXJraW50Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3MDA3NTg5ODUsImV4cCI6MjAxNjMzNDk4NX0.5I0BIP35xzqb0lgCNA6xRWxj2IUmcaQ9AmexdD0WGVs"
        supabaseUrl = "https://msrymwiawamltodzgdyr.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im1zcnltd2lhd2FtbHRvZHpnZHlyIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MDA2NDg2NDYsImV4cCI6MjAxNjIyNDY0Nn0.3PrDR_z3J8nWHIrzlBjhRuJmN235jgDydjUX8Xlj01s"
    ) {
        install(GoTrue)
        install(Postgrest)
    }
}