package ru.avdonin;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Person {
    private String name;
    private double age;
    private boolean isMale;
    private List<Pet> pets;
}
/*
ru.avdonin.Person=[
    name=[Паша]
    age=[23.5]
    isMale=[true]
    pets=[
        ru.avdonin.Pet=[
            age=[1.3]
            name=[Пушок]
        ]
        ru.avdonin.Pet=[
            age=[2.6]
            name=[Ласточка]
        ]
    ]
]
 */
