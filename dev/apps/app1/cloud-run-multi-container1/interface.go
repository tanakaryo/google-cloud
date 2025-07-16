package main

import "fmt"

type Animal interface {
	Speak() string
}

type Cat struct {
	Name string
}

type Dog struct {
	Name string
}

func (d Dog) Speak() string {
	return d.Name + " says: One!"
}

func (c Cat) Speak() string {
	return c.Name + " says: Nyan!"
}

func MakeItSpeak(a Animal) {
	fmt.Println(a.Speak())
}

func main() {
	dog := Dog{Name: "Pochi"}
	cat := Cat{Name: "Tama"}

	MakeItSpeak(dog)
	MakeItSpeak(cat)
}
