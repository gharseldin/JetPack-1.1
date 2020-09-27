**Adding Some Architecture**
----------------------------
****
**Repositories**
- Google's recommended architecture pattern.
- Handles network, I/O
- Typically a Singleton object
- This creates isolation, mostly for testing purposes.
- Data sorces can be used as another layer of abstraction
  - To add in-memory caching at the repository level
  - support local I/O (for persistent cache) and network I/O (the source of shared or updated data)
  - could be defined during compile time 

**Unidirectional Data Flow**
 
- When a user does something in the UI, such as click a toolbar button, the UI calls a function on something called “the motor”
- In response to those calls, the motor asks the repository to read, update, or delete some data, using some sort of asynchronous mechanism
- The result of the repository work gets delivered to the motor via that asynchronous mechanism
- The motor emits a “view state” of details that get used by the UI to update
what the user sees

**Components needed**
1. View State (Preferably sealed class in Kotlin)
   1. loading
   2. error
   3. data
2. Repository
   1. suspend functions with coroutines
   2. AtomicReferences if required for safe access
   3. Maybe extension functions
3. Motor
   1. Will have LiveData exposed (so no one can mutate it) and a private MutableLiveData to update it internally.
4. Activity or Fragment
   1. Instantiate the motor
   2. Observe the LiveData and adjust UI elements accordingly

**States and Events**

- A state is something that we want to survive a configuration change and use
- An event is something that we want to use exactly once, regardless of any
configuration changes that may or may not occur


