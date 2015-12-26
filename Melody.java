//Blake Denniston
//CSE 143 Section BK - Hunter Schafer
//
//Description: This program loads a text file and can play it, append another song to it, 
//get the length in seconds of the song, reverse the song, and print it out.  It takes into
//account repeated sections defined in the text file.

import java.util.*;

public class Melody {
   private Queue<Note> song = new LinkedList<Note>();
   private double length;
   private int repeatCount;
   
   //constructs the note queue and calculates the length field, taking into account repeated notes
   //takes in the given queue of notes from the text file
   public Melody(Queue<Note> song) {
      this.song = song;
      
      for(int i = 0; i < song.size(); i++) {
         length += song.peek().getDuration();
         if(song.peek().isRepeat()) {
            length += song.peek().getDuration();
            repeatCount++;
         } else if (repeatCount % 2 != 0) {
            length += song.peek().getDuration();
         }         
         song.add(song.remove());
         
      }
   }
   
   //returns length as a double
   public double getLength() {
      return length;
   }
   
   //prints the song out according to the toString method found in the note class
   public String toString() {
      String result = "";
      for(int i = 0; i < song.size(); i++) {
         result += song.peek().toString() + "\n";
         song.add(song.remove());
      }
      return result;
   }
   
   //takes in the double tempo as a parameter and changes the length, each note's length,
   //accordingly.  Multiple tempos stack on top of each other(tempo .5 * another tempo .5 = 
   //a new tempo of .25)
   public void changeTempo(double tempo) {
      for(int i = 0; i < song.size(); i++) {
         Note durationNote = song.remove();
         durationNote.setDuration(durationNote.getDuration() * tempo);        
         song.add(durationNote);
      }
      this.length *= tempo;
   }
   
   //reverses the order of the song
   public void reverse(){
      Stack<Note> tempStack = new Stack<Note>();
      //queue to stack
      while(!song.isEmpty()) {
         tempStack.push(song.remove());      
      }      
      //stack to queue (in reversed order)
      while(!tempStack.isEmpty()) {
         song.add(tempStack.pop());
      }
   }
   
   //adds a given song onto the end of the current loaded song,
   //updates length (pays attention to repeated notes in the new song)
   //take in a melody object as a paramter (the new song)
   public void append(Melody other) {   
      for(int i = 0; i < other.song.size(); i++) {
         this.song.add(other.song.peek());
         other.song.add(other.song.remove());
      }
      this.length += other.length;      
   }
   
   //plays the song, including repeated notes
   public void play() {
      Queue<Note> repeat = new LinkedList<Note>();
      
      for(int i = 0; i < song.size(); i++) {
         Note temporaryNote = song.remove();
         temporaryNote.play();
         if(temporaryNote.isRepeat()) {
            repeat.add(temporaryNote);
            repeatCount++;
         } else if(repeatCount % 2 != 0) {
            repeat.add(temporaryNote);
         }
         if(repeatCount % 2 == 0) {
            while(!repeat.isEmpty()) {
               Note tempRemove = repeat.remove();
               tempRemove.play();
            }
         }      
         song.add(temporaryNote);
      }         
   }
}