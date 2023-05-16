package com.example.matrimony

import android.content.Context
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matrimony.db.entities.*
import com.example.matrimony.db.repository.*
import com.example.matrimony.utils.getDateFromString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import javax.inject.Inject

@HiltViewModel
class InjectDataViewModel
@Inject
constructor(
    private val injectDataRepository: InjectDataRepository,
    private val habitsRepository: HabitsRepository,
    private val familyDetailsRepository: FamilyDetailsRepository,
    private val albumRepository: AlbumRepository,
    private val privacySettingsRepository: PrivacySettingsRepository,
    private val successStoriesRepository: SuccessStoriesRepository

) :
    ViewModel() {

    lateinit var context: Context

    suspend fun addAccount() {
        Log.i(TAG, "initAccount")

        val emailArray = arrayOf(
            "simbu@gmail.com",
            "anirudh@gmail.com",
            "gvp@gmail.com",
            "yuvan@gmail.com",
            "nani@gmail.com",
            "dulquer@gmail.com",
            "harish@gmail.com",
            "siva@gmail.com",
            "sidhu@gmail.com",
            "hrithik@gmail.com",
            "tovino@gmail.com",
            "dhanush@gmail.com",
            "atharva@gmail.com",
            "makapa@gmail.com",
            "ashok@gmail.com",
            "trisha@gmail.com",
            "megha@gmail.com",
            "sridivya@gmail.com",
            "anupama@gmail.com",
            "amy@gmail.com",
            "shreya@gmail.com",
            "nazriya@gmail.com",
            "priyankamohan@gmail.com",
            "priya@gmail.com",
            "vani@gmail.com",
            "shwetha@gmail.com",
            "samantha@gmail.com",
            "priyankadesh@gmail.com",
            "sai@gmail.com",
            "deepika@gmail.com",
            "keerthi@gmail.com",

            "simbu1@gmail.com",
            "anirudh1@gmail.com",
            "gvp1@gmail.com",
            "yuvan1@gmail.com",
            "nani1@gmail.com",
            "dulquer1@gmail.com",
            "harish1@gmail.com",
            "siva1@gmail.com",
            "sidhu1@gmail.com",
            "hrithik1@gmail.com",
            "tovino1@gmail.com",
            "dhanush1@gmail.com",
            "atharva1@gmail.com",
            "makapa1@gmail.com",
            "ashok1@gmail.com",
            "trisha1@gmail.com",
            "megha1@gmail.com",
            "sridivya1@gmail.com",
            "anupama1@gmail.com",
            "amy1@gmail.com",
            "shreya1@gmail.com",
            "nazriya1@gmail.com",
            "priyankamohan1@gmail.com",
            "priya1@gmail.com",
            "vani1@gmail.com",
            "shwetha1@gmail.com",
            "samantha1@gmail.com",
            "priyankadesh1@gmail.com",
            "sai1@gmail.com",
            "deepika1@gmail.com",
            "keerthi1@gmail.com",
        )
        val mobileArray = arrayOf(
            "8248471682",
            "9999999990",
            "9999999991",
            "9999999992",
            "9999999993",
            "9999999994",
            "9999999995",
            "9999999996",
            "9999999997",
            "9999999998",
            "9999999999",
            "9999999910",
            "9999999911",
            "9999999912",
            "9999999913",
            "9092731796",
            "9999999914",
            "9999999915",
            "9999999916",
            "9999999917",
            "9999999918",
            "9999999919",
            "9999999920",
            "9999999921",
            "9999999922",
            "9999999923",
            "9999999924",
            "9999999925",
            "9999999926",
            "9999999927",
            "9999999928",

            "9248471682",
            "8999999990",
            "8999999991",
            "8999999992",
            "8999999993",
            "8999999994",
            "8999999995",
            "8999999996",
            "8999999997",
            "8999999998",
            "8999999999",
            "8999999910",
            "8999999911",
            "8999999912",
            "8999999913",
            "8092731796",
            "8999999914",
            "8999999915",
            "8999999916",
            "8999999917",
            "8999999918",
            "8999999919",
            "8999999920",
            "8999999921",
            "8999999922",
            "8999999923",
            "8999999924",
            "8999999925",
            "8999999926",
            "8999999927",
            "8999999928",
        )
        val accountsArray = Array<Account>(62) {
            Account(email = emailArray[it], mobile_no = mobileArray[it], password = "Qwerty@123")
        }

//        viewModelScope.launch {

        accountsArray.forEach {
            injectDataRepository.addAccount(it)
        }

        for (i in 1..62) {
            privacySettingsRepository.addPrivacySettings(
                PrivacySettings(
                    i,
                    "Everyone",
//                    if(i%3==0)"Connections Only" else "Everyone",
                    if (i % 3 == 0) "Everyone" else "Connections Only",
                    "Everyone"
                )
            )
        }
//        }

        Log.i(TAG,"Accounts Loaded")
    }

    suspend fun addUsers() {
        val nameArr = arrayOf(
            "Silambarasan",
            "Anirudh",
            "GV Prakash",
            "Yuvan Raja",
            "Nani",
            "Dulquer",
            "Harish Kalyan",
            "Siva Karthikeyan",
            "Siddharth",
            "Hrithik Roshan",
            "Tovino Thomas",
            "Dhanush",
            "Atharva Murali",
            "Ma Ka Pa Anand",
            "Ashok Selvan",
            "Trisha",
            "Megha Akash",
            "Sri Divya",
            "Anupama",
            "Amy Jackson",
            "Shreya Goshal",
            "Nazriya Nazim",
            "Priyanka Mohan",
            "Priya Bhavani",
            "Vani Bhojan",
            "Shwetha Mohan",
            "Samantha",
            "Priyanka",
            "Sai Pallavi",
            "Deepika Padukone",
            "Keerthy Suresh",


            "Silambarasan",
            "Anirudh",
            "GV Prakash",
            "Yuvan Raja",
            "Nani",
            "Dulquer",
            "Harish Kalyan",
            "Siva Karthikeyan",
            "Siddharth",
            "Hrithik Roshan",
            "Tovino Thomas",
            "Dhanush",
            "Atharva Murali",
            "Ma Ka Pa Anand",
            "Ashok Selvan",
            "Trisha",
            "Megha Akash",
            "Sri Divya",
            "Anupama",
            "Amy Jackson",
            "Shreya Goshal",
            "Nazriya Nazim",
            "Priyanka Mohan",
            "Priya Bhavani",
            "Vani Bhojan",
            "Shwetha Mohan",
            "Samantha",
            "Priyanka",
            "Sai Pallavi",
            "Deepika Padukone",
            "Keerthy Suresh",
        )
        val genderArr = arrayOf(
            "M",
            "M",
            "M",
            "M",
            "M",
            "M",
            "M",
            "M",
            "M",
            "M",
            "M",
            "M",
            "M",
            "M",
            "M",
            "F",
            "F",
            "F",
            "F",
            "F",
            "F",
            "F",
            "F",
            "F",
            "F",
            "F",
            "F",
            "F",
            "F",
            "F",
            "F",

            "M",
            "M",
            "M",
            "M",
            "M",
            "M",
            "M",
            "M",
            "M",
            "M",
            "M",
            "M",
            "M",
            "M",
            "M",
            "F",
            "F",
            "F",
            "F",
            "F",
            "F",
            "F",
            "F",
            "F",
            "F",
            "F",
            "F",
            "F",
            "F",
            "F",
            "F",
        )

        val dobArr = arrayOf(
            getDateFromString("03/02/1983")!!,
            getDateFromString("16/10/1990")!!,
            getDateFromString("13/06/1987")!!,
            getDateFromString("31/08/1979")!!,
            getDateFromString("24/02/1982")!!,
            getDateFromString("28/07/1986")!!,
            getDateFromString("29/06/1990")!!,
            getDateFromString("17/02/1985")!!,
            getDateFromString("17/04/1979")!!,
            getDateFromString("10/01/1980")!!,
            getDateFromString("21/01/1989")!!,
            getDateFromString("28/07/1983")!!,
            getDateFromString("07/05/1989")!!,
            getDateFromString("20/05/1986")!!,
            getDateFromString("08/11/1989")!!,
            getDateFromString("04/05/1983")!!,
            getDateFromString("26/10/1995")!!,
            getDateFromString("01/04/1993")!!,
            getDateFromString("18/02/1996")!!,
            getDateFromString("31/01/1992")!!,
            getDateFromString("12/03/1984")!!,
            getDateFromString("20/12/1994")!!,
            getDateFromString("20/11/1994")!!,
            getDateFromString("31/12/1989")!!,
            getDateFromString("28/10/1988")!!,
            getDateFromString("19/11/1985")!!,
            getDateFromString("28/04/1987")!!,
            getDateFromString("28/04/1992")!!,
            getDateFromString("09/05/1992")!!,
            getDateFromString("05/01/1986")!!,
            getDateFromString("17/10/1992")!!,


            getDateFromString("03/02/1983")!!,
            getDateFromString("16/10/1990")!!,
            getDateFromString("13/06/1987")!!,
            getDateFromString("31/08/1979")!!,
            getDateFromString("24/02/1982")!!,
            getDateFromString("28/07/1986")!!,
            getDateFromString("29/06/1990")!!,
            getDateFromString("17/02/1985")!!,
            getDateFromString("17/04/1979")!!,
            getDateFromString("10/01/1980")!!,
            getDateFromString("21/01/1989")!!,
            getDateFromString("28/07/1983")!!,
            getDateFromString("07/05/1989")!!,
            getDateFromString("20/05/1986")!!,
            getDateFromString("08/11/1989")!!,
            getDateFromString("04/05/1983")!!,
            getDateFromString("26/10/1995")!!,
            getDateFromString("01/04/1993")!!,
            getDateFromString("18/02/1996")!!,
            getDateFromString("31/01/1992")!!,
            getDateFromString("12/03/1984")!!,
            getDateFromString("20/12/1994")!!,
            getDateFromString("20/11/1994")!!,
            getDateFromString("31/12/1989")!!,
            getDateFromString("28/10/1988")!!,
            getDateFromString("19/11/1985")!!,
            getDateFromString("28/04/1987")!!,
            getDateFromString("28/04/1992")!!,
            getDateFromString("09/05/1992")!!,
            getDateFromString("05/01/1986")!!,
            getDateFromString("17/10/1992")!!,
        )

//        val dobArr = Array<Date>(12) {
//            val localDate = LocalDate.of(2004 - it, 12 - it, 30 - it)
//            val date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
//            Log.i(TAG, date.toString())
//            date
//        }
        val religionArr = arrayOf(
            "Hindu",
            "Christian",
            "Hindu",
            "Muslim",
            "Atheism",
            "Muslim",
            "Hindu",
            "Christian",
            "Atheism",
            "Muslim",
            "Christian",
            "Hindu",
            "Hindu",
            "Christian",
            "Hindu",
            "Hindu",
            "Muslim",
            "Muslim",
            "Hindu",
            "Christian",
            "Hindu",
            "Muslim",
            "Hindu",
            "Atheism",
            "Hindu",
            "Hindu",
            "Christian",
            "Hindu",
            "Muslim",
            "Atheism",
            "Hindu",

            "Hindu",
            "Christian",
            "Hindu",
            "Muslim",
            "Atheism",
            "Muslim",
            "Hindu",
            "Christian",
            "Atheism",
            "Muslim",
            "Christian",
            "Hindu",
            "Hindu",
            "Christian",
            "Hindu",
            "Hindu",
            "Muslim",
            "Muslim",
            "Hindu",
            "Christian",
            "Hindu",
            "Muslim",
            "Hindu",
            "Atheism",
            "Hindu",
            "Hindu",
            "Christian",
            "Hindu",
            "Muslim",
            "Atheism",
            "Hindu",
        )
        val casteArr = arrayOf(
            "Nadar",
            "Adventist",
            "Gounder",
            "Ansari",
            null,
            "Lebbai",
            "Sourashtra",
            "Roman Catholic",
            null,
            "Hanafi",
            "Baptist",
            "Others",
            "Others",
            "Latin Catholic",
            "Brahmin",
            "Chettiyar",
            "Malik",
            "Lebbai",
            "Gounder",
            "Unspecified",
            "Unspecified",
            "Unspecified",
            "Nadar",
            null,
            "Sourashtra",
            "Others",
            "Evangelist",
            "Gounder",
            "Lebbai",
            null,
            "Chettiyar",

            "Nadar",
            "Adventist",
            "Gounder",
            "Ansari",
            null,
            "Lebbai",
            "Sourashtra",
            "Roman Catholic",
            null,
            "Hanafi",
            "Baptist",
            "Others",
            "Others",
            "Latin Catholic",
            "Brahmin",
            "Chettiyar",
            "Malik",
            "Lebbai",
            "Gounder",
            "Unspecified",
            "Unspecified",
            "Unspecified",
            "Nadar",
            null,
            "Sourashtra",
            "Others",
            "Evangelist",
            "Gounder",
            "Lebbai",
            null,
            "Chettiyar",
        )
        val zodiacArr = arrayOf(
            "None",
            "Aries",
            "Taurus",
            "Gemini",
            "Cancer",
            "Leo",
            "Vigro",
            "Libra",
            "Scorpio",
            "Sagittarius",
            "Capricorn",
            "Aquarius",
            "Pisces",
            "None",
            "Aries",
            "Taurus",
            "Gemini",
            "Cancer",
            "Leo",
            "Vigro",
            "Libra",
            "Scorpio",
            "Sagittarius",
            "Capricorn",
            "Aquarius",
            "Pisces",
            "Capricorn",
            "Aquarius",
            "None",
            "Cancer",
            "Leo",


            "None",
            "Aries",
            "Taurus",
            "Gemini",
            "Cancer",
            "Leo",
            "Vigro",
            "Libra",
            "Scorpio",
            "Sagittarius",
            "Capricorn",
            "Aquarius",
            "Pisces",
            "None",
            "Aries",
            "Taurus",
            "Gemini",
            "Cancer",
            "Leo",
            "Vigro",
            "Libra",
            "Scorpio",
            "Sagittarius",
            "Capricorn",
            "Aquarius",
            "Pisces",
            "Capricorn",
            "Aquarius",
            "None",
            "Cancer",
            "Leo",
        )
        val starArr = arrayOf(
            "None",
            "Ashwini",
            "Bharani",
            "Krittika",
            "Rohini",
            "Mrigashira",
            "Ardra",
            "Punarvasu",
            "Pushya",
            "Ashlesha",
            "Magha",
            "Purva Phalguni",
            "Uttara Phalguni",
            "Hasta",
            "Chitra",
            "Swati",
            "Vishakha",
            "Anuradha",
            "Jyeshtha",
            "Mula",
            "Purva Ashadha",
            "Uttara Ashadha",
            "Shravana",
            "Shravana",
            "Dhanishta",
            "Shatabhisha",
            "Purva Bhadrapada",
            "Uttara Bhadrapada",
            "Uttara Bhadrapada",
            "Revati",
            "Revati",


            "None",
            "Ashwini",
            "Bharani",
            "Krittika",
            "Rohini",
            "Mrigashira",
            "Ardra",
            "Punarvasu",
            "Pushya",
            "Ashlesha",
            "Magha",
            "Purva Phalguni",
            "Uttara Phalguni",
            "Hasta",
            "Chitra",
            "Swati",
            "Vishakha",
            "Anuradha",
            "Jyeshtha",
            "Mula",
            "Purva Ashadha",
            "Uttara Ashadha",
            "Shravana",
            "Shravana",
            "Dhanishta",
            "Shatabhisha",
            "Purva Bhadrapada",
            "Uttara Bhadrapada",
            "Uttara Bhadrapada",
            "Revati",
            "Revati",
        )

        val stateArray = arrayOf(
            "Andhra Pradesh",
            "Karnataka",
            "Kerala",
            "Tamilnadu",
            "Others",
            "Andhra Pradesh",
            "Karnataka",
            "Kerala",
            "Tamilnadu",
            "Others",
            "Andhra Pradesh",
            "Karnataka",
            "Kerala",
            "Tamilnadu",
            "Others",
            "Andhra Pradesh",
            "Karnataka",
            "Kerala",
            "Tamilnadu",
            "Others",
            "Andhra Pradesh",
            "Karnataka",
            "Kerala",
            "Tamilnadu",
            "Others",
            "Andhra Pradesh",
            "Karnataka",
            "Kerala",
            "Tamilnadu",
            "Others",
            "Tamilnadu",


            "Andhra Pradesh",
            "Karnataka",
            "Kerala",
            "Tamilnadu",
            "Others",
            "Andhra Pradesh",
            "Karnataka",
            "Kerala",
            "Tamilnadu",
            "Others",
            "Andhra Pradesh",
            "Karnataka",
            "Kerala",
            "Tamilnadu",
            "Others",
            "Andhra Pradesh",
            "Karnataka",
            "Kerala",
            "Tamilnadu",
            "Others",
            "Andhra Pradesh",
            "Karnataka",
            "Kerala",
            "Tamilnadu",
            "Others",
            "Andhra Pradesh",
            "Karnataka",
            "Kerala",
            "Tamilnadu",
            "Others",
            "Tamilnadu"
        )
        val cityArray = arrayOf(
            "Amaravati",
            "Bangalore",
            "Aluva",
            "Chennai",
            null,
            "Guntur",
            "Hubbali",
            "Cochin",
            "Coimbatore",
            null,
            "Nellore",
            "Mangaluru",
            "Kollam",
            "Madurai",
            null,
            "Tirupati",
            "Mysore",
            "Kozhikode",
            "Ooty",
            null,
            "Visakhapatnam",
            "Udupi",
            "Tiruvanandapuram",
            "Nagercoil",
            null,
            "Others",
            "Others",
            "Others",
            "Others",
            null,
            "Chennai",

            "Amaravati",
            "Bangalore",
            "Aluva",
            "Chennai",
            null,
            "Guntur",
            "Hubbali",
            "Cochin",
            "Coimbatore",
            null,
            "Nellore",
            "Mangaluru",
            "Kollam",
            "Madurai",
            null,
            "Tirupati",
            "Mysore",
            "Kozhikode",
            "Ooty",
            null,
            "Visakhapatnam",
            "Udupi",
            "Tiruvanandapuram",
            "Nagercoil",
            null,
            "Others",
            "Others",
            "Others",
            "Others",
            null,
            "Chennai",
        )
        val heightArray = arrayOf(
            "4 ft 6 in",
            "4 ft 7 in",
            "4 ft 8 in",
            "4 ft 9 in",
            "4 ft 10 in",
            "4 ft 11 in",
            "5 ft",
            "5 ft 1 in",
            "5 ft 2 in",
            "5 ft 3 in",
            "5 ft 4 in",
            "5 ft 5 in",
            "5 ft 6 in",
            "5 ft 7 in",
            "5 ft 8 in",
            "5 ft 9 in",
            "5 ft 10 in",
            "5 ft 11 in",
            "6 ft",
            "4 ft 6 in",
            "4 ft 7 in",
            "4 ft 8 in",
            "4 ft 9 in",
            "4 ft 10 in",
            "4 ft 11 in",
            "5 ft",
            "5 ft 1 in",
            "5 ft 2 in",
            "5 ft 3 in",
            "5 ft 4 in",
            "5 ft 5 in",


            "4 ft 6 in",
            "4 ft 7 in",
            "4 ft 8 in",
            "4 ft 9 in",
            "4 ft 10 in",
            "4 ft 11 in",
            "5 ft",
            "5 ft 1 in",
            "5 ft 2 in",
            "5 ft 3 in",
            "5 ft 4 in",
            "5 ft 5 in",
            "5 ft 6 in",
            "5 ft 7 in",
            "5 ft 8 in",
            "5 ft 9 in",
            "5 ft 10 in",
            "5 ft 11 in",
            "6 ft",
            "4 ft 6 in",
            "4 ft 7 in",
            "4 ft 8 in",
            "4 ft 9 in",
            "4 ft 10 in",
            "4 ft 11 in",
            "5 ft",
            "5 ft 1 in",
            "5 ft 2 in",
            "5 ft 3 in",
            "5 ft 4 in",
            "5 ft 5 in",
        )

        val physicalStatusArray = arrayOf(
            "Normal",
            "Normal",
            "Normal",
            "Normal",
            "Normal",
            "Normal",
            "Normal",
            "Normal",
            "Normal",
            "Normal",
            "Normal",
            "Normal"
        )
        val educationArray = arrayOf(
            "B.E",
            "B.Tech",
            "MBBS",
            "B.Arch",
            "B.Sc",
            "B.E",
            "B.Tech",
            "MBBS",
            "B.Arch",
            "B.Sc",
            "Others",
            "Others",
            "MBBS",
            "B.E",
            "B.Tech",
            "B.Arch",
            "B.Sc",
            "B.E",
            "B.Tech",
            "MBBS",
            "B.Arch",
            "B.Sc",
            "Others",
            "Others",
            "B.E",
            "B.Tech",
            "B.Arch",
            "B.Sc",
            "MBBS",
            "B.E",
            "MBBS",


            "B.E",
            "B.Tech",
            "MBBS",
            "B.Arch",
            "B.Sc",
            "B.E",
            "B.Tech",
            "MBBS",
            "B.Arch",
            "B.Sc",
            "Others",
            "Others",
            "MBBS",
            "B.E",
            "B.Tech",
            "B.Arch",
            "B.Sc",
            "B.E",
            "B.Tech",
            "MBBS",
            "B.Arch",
            "B.Sc",
            "Others",
            "Others",
            "B.E",
            "B.Tech",
            "B.Arch",
            "B.Sc",
            "MBBS",
            "B.E",
            "MBBS",
        )
        val employedInArray = arrayOf(
            "Government",
            "Private",
            "Government",
            "Private",
            "Government",
            "Private",
            "Government",
            "Private",
            "Government",
            "Private",
            "Government",
            "Private",
            "Government",
            "Private",
            "Government",
            "Private",
            "Government",
            "Private",
            "Government",
            "Private",
            "Government",
            "Private",
            "Government",
            "Private",
            "Government",
            "Private",
            "Government",
            "Private",
            "Government",
            "Private",
            "Government",


            "Government",
            "Private",
            "Government",
            "Private",
            "Government",
            "Private",
            "Government",
            "Private",
            "Government",
            "Private",
            "Government",
            "Private",
            "Government",
            "Private",
            "Government",
            "Private",
            "Government",
            "Private",
            "Government",
            "Private",
            "Government",
            "Private",
            "Government",
            "Private",
            "Government",
            "Private",
            "Government",
            "Private",
            "Government",
            "Private",
            "Government",
        )
        val occupationArray = arrayOf(
            "Accountant",
            "Entrepreneur",
            "Doctor",
            "Manager",
            "Marketing Professional",
            "Software Professional",
            "Studying",
            "Doctor",
            "Supervisor",
            "Technician",
            "Others",
            "Accountant",
            "Doctor",
            "Manager",
            "Marketing Professional",
            "Software Professional",
            "Studying",
            "Supervisor",
            "Technician",
            "Doctor",
            "Accountant",
            "Others",
            "Entrepreneur",
            "Manager",
            "Marketing Professional",
            "Software Professional",
            "Studying",
            "Supervisor",
            "Doctor",
            "Others",
            "Doctor",


            "Accountant",
            "Entrepreneur",
            "Doctor",
            "Manager",
            "Marketing Professional",
            "Software Professional",
            "Studying",
            "Doctor",
            "Supervisor",
            "Technician",
            "Others",
            "Accountant",
            "Doctor",
            "Manager",
            "Marketing Professional",
            "Software Professional",
            "Studying",
            "Supervisor",
            "Technician",
            "Doctor",
            "Accountant",
            "Others",
            "Entrepreneur",
            "Manager",
            "Marketing Professional",
            "Software Professional",
            "Studying",
            "Supervisor",
            "Doctor",
            "Others",
            "Doctor",
        )
        val annualIncomeArray = arrayOf(
            "3-4 Lakhs",
            "4-5 Lakhs",
            "5-6 Lakhs",
            "6-7 Lakhs",
            "7-8 Lakhs",
            "8-9 Lakhs",
            "9-10 Lakhs",
            "Above 10 Lakhs",
            "3-4 Lakhs",
            "4-5 Lakhs",
            "5-6 Lakhs",
            "6-7 Lakhs",
            "7-8 Lakhs",
            "8-9 Lakhs",
            "9-10 Lakhs",
            "Above 10 Lakhs",
            "3-4 Lakhs",
            "4-5 Lakhs",
            "5-6 Lakhs",
            "6-7 Lakhs",
            "7-8 Lakhs",
            "8-9 Lakhs",
            "9-10 Lakhs",
            "Above 10 Lakhs",
            "3-4 Lakhs",
            "4-5 Lakhs",
            "5-6 Lakhs",
            "6-7 Lakhs",
            "7-8 Lakhs",
            "9-10 Lakhs",
            "Above 10 Lakhs",


            "3-4 Lakhs",
            "4-5 Lakhs",
            "5-6 Lakhs",
            "6-7 Lakhs",
            "7-8 Lakhs",
            "8-9 Lakhs",
            "9-10 Lakhs",
            "Above 10 Lakhs",
            "3-4 Lakhs",
            "4-5 Lakhs",
            "5-6 Lakhs",
            "6-7 Lakhs",
            "7-8 Lakhs",
            "8-9 Lakhs",
            "9-10 Lakhs",
            "Above 10 Lakhs",
            "3-4 Lakhs",
            "4-5 Lakhs",
            "5-6 Lakhs",
            "6-7 Lakhs",
            "7-8 Lakhs",
            "8-9 Lakhs",
            "9-10 Lakhs",
            "Above 10 Lakhs",
            "3-4 Lakhs",
            "4-5 Lakhs",
            "5-6 Lakhs",
            "6-7 Lakhs",
            "7-8 Lakhs",
            "9-10 Lakhs",
            "Above 10 Lakhs",
        )

        val familyTypeArray =Array<String>(62){
            if (it%2==0) "Joint" else "Nuclear"
        }


        val profilePicArray = arrayOf(
            BitmapFactory.decodeResource(context.resources, R.drawable.simbu4_19),
            BitmapFactory.decodeResource(context.resources, R.drawable.anirudh3_13),
            BitmapFactory.decodeResource(context.resources, R.drawable.gvp2_11),
            BitmapFactory.decodeResource(context.resources, R.drawable.yuvan2_1),
            BitmapFactory.decodeResource(context.resources, R.drawable.nani4_17),
            BitmapFactory.decodeResource(context.resources, R.drawable.dulquer1_5),
            BitmapFactory.decodeResource(context.resources, R.drawable.harish3_17),
            BitmapFactory.decodeResource(context.resources, R.drawable.siva2_16),
            BitmapFactory.decodeResource(context.resources, R.drawable.siddharth2_9),
            BitmapFactory.decodeResource(context.resources, R.drawable.hrithik1_19),
            BitmapFactory.decodeResource(context.resources, R.drawable.tovino1_3),
            BitmapFactory.decodeResource(context.resources, R.drawable.dhanush4_4),
            BitmapFactory.decodeResource(context.resources, R.drawable.adharva1),
            BitmapFactory.decodeResource(context.resources, R.drawable.makapa1_6),
            BitmapFactory.decodeResource(context.resources, R.drawable.ashok3_21),

            BitmapFactory.decodeResource(context.resources, R.drawable.trisha1_7),
            BitmapFactory.decodeResource(context.resources, R.drawable.megha1_10),
            BitmapFactory.decodeResource(context.resources, R.drawable.sri4_1),
            BitmapFactory.decodeResource(context.resources, R.drawable.anupama2_16),
            BitmapFactory.decodeResource(context.resources, R.drawable.amy2),
            BitmapFactory.decodeResource(context.resources, R.drawable.shreya4_3),
            BitmapFactory.decodeResource(context.resources, R.drawable.nazriya3_1),
            BitmapFactory.decodeResource(context.resources, R.drawable.priyanka2_12),
            BitmapFactory.decodeResource(context.resources, R.drawable.priya1_7),
            BitmapFactory.decodeResource(context.resources, R.drawable.vani6),
            BitmapFactory.decodeResource(context.resources, R.drawable.shwetha1_4),
            BitmapFactory.decodeResource(context.resources, R.drawable.samantha5),
            BitmapFactory.decodeResource(context.resources, R.drawable.priyankadesh1_15),
            BitmapFactory.decodeResource(context.resources, R.drawable.pallavi4_6),
            BitmapFactory.decodeResource(context.resources, R.drawable.deepika1_23),
            BitmapFactory.decodeResource(context.resources, R.drawable.keerthy5),


            BitmapFactory.decodeResource(context.resources, R.drawable.simbu4_19),
            BitmapFactory.decodeResource(context.resources, R.drawable.anirudh3_13),
            BitmapFactory.decodeResource(context.resources, R.drawable.gvp2_11),
            BitmapFactory.decodeResource(context.resources, R.drawable.yuvan2_1),
            BitmapFactory.decodeResource(context.resources, R.drawable.nani4_17),
            BitmapFactory.decodeResource(context.resources, R.drawable.dulquer1_5),
            BitmapFactory.decodeResource(context.resources, R.drawable.harish3_17),
            BitmapFactory.decodeResource(context.resources, R.drawable.siva2_16),
            BitmapFactory.decodeResource(context.resources, R.drawable.siddharth2_9),
            BitmapFactory.decodeResource(context.resources, R.drawable.hrithik1_19),
            BitmapFactory.decodeResource(context.resources, R.drawable.tovino1_3),
            BitmapFactory.decodeResource(context.resources, R.drawable.dhanush4_4),
            BitmapFactory.decodeResource(context.resources, R.drawable.adharva1),
            BitmapFactory.decodeResource(context.resources, R.drawable.makapa1_6),
            BitmapFactory.decodeResource(context.resources, R.drawable.ashok3_21),

            BitmapFactory.decodeResource(context.resources, R.drawable.trisha1_7),
            BitmapFactory.decodeResource(context.resources, R.drawable.megha1_10),
            BitmapFactory.decodeResource(context.resources, R.drawable.sri4_1),
            BitmapFactory.decodeResource(context.resources, R.drawable.anupama2_16),
            BitmapFactory.decodeResource(context.resources, R.drawable.amy2),
            BitmapFactory.decodeResource(context.resources, R.drawable.shreya4_3),
            BitmapFactory.decodeResource(context.resources, R.drawable.nazriya3_1),
            BitmapFactory.decodeResource(context.resources, R.drawable.priyanka2_12),
            BitmapFactory.decodeResource(context.resources, R.drawable.priya1_7),
            BitmapFactory.decodeResource(context.resources, R.drawable.vani6),
            BitmapFactory.decodeResource(context.resources, R.drawable.shwetha1_4),
            BitmapFactory.decodeResource(context.resources, R.drawable.samantha5),
            BitmapFactory.decodeResource(context.resources, R.drawable.priyankadesh1_15),
            BitmapFactory.decodeResource(context.resources, R.drawable.pallavi4_6),
            BitmapFactory.decodeResource(context.resources, R.drawable.deepika1_23),
            BitmapFactory.decodeResource(context.resources, R.drawable.keerthy5),
        )

//        albumRepository.addAlbum(Album(0, 1, BitmapFactory.decodeResource(context.resources, R.drawable.naruto_img5)))

        for (i in 1..62) {
            albumRepository.addAlbum(Album(0, i, profilePicArray[i - 1], true))
        }
        addAlbum()

        val userArray = Array<User>(62) {
            User(
                0,
                nameArr[it],
                genderArr[it],
                dobArr[it],
                religionArr[it],
                "English",
                "Never Married",
                null,
                casteArr[it],
                zodiacArr[it],
                starArr[it],
                "India",
                stateArray[it],
                cityArray[it],
                heightArray[it],
//                BitmapFactory.decodeResource(context.resources, R.drawable.default_profile_pic),
                profilePicArray[it],
                "Normal",
                educationArray[it],
                employedInArray[it],
                occupationArray[it],
                annualIncomeArray[it],
                "Middle Class",
                familyTypeArray[it],
                "Not Set"
            )
        }


        userArray.forEach {
            Log.i(TAG, it.toString())
            injectDataRepository.addUser(it)
        }

        Log.i(TAG,"Users Loaded")
    }

    private suspend fun addAlbum() {
//        albumRepository.addAlbum(Album(0, 1, BitmapFactory.decodeResource(context.resources, R.drawable.simbu1_12)))
//        albumRepository.addAlbum(Album(0, 1, BitmapFactory.decodeResource(context.resources, R.drawable.simbu2_13)))
        albumRepository.addAlbum(
            Album(
                0,
                1,
                BitmapFactory.decodeResource(context.resources, R.drawable.simbu3_14)
            )
        )


    }

    suspend fun addHabits() {
        val habitsArray = mutableListOf<Habits>()

        for (i in 1..62) {
//            habitsArray.add(Habits(1,"Never","Occasionally","Non-Vegetarian"))
            habitsRepository.insertHabit(Habits(i, "Never", "Occasionally", "Non-Vegetarian"))
        }

        Log.i(TAG,"Habits Loaded")

    }

    suspend fun addFamilyDetails() {
//        val habitsArray = mutableListOf<Habits>()

        for (i in 1..62) {
//            habitsArray.add(Habits(1,"Never","Occasionally","Non-Vegetarian"))
            familyDetailsRepository.setFamilyDetails(
                FamilyDetails(
                    i,
                    "John Abraham",
                    "Jennifer Lawrence",
                    1,
                    0,
                    2,
                    1
                )
            )
//            habitsRepository.insertHabit(Habits(i, "Never", "Occasionally", "Non-Vegetarian"))
        }
//        addSuccessStories()

        Log.i(TAG,"Family Details Loaded")
    }



}