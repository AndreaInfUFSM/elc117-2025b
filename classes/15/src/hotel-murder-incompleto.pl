% Pessoas: Alice, Bob, Carol, Dave
% Suítes do oitavo andar: 801, 802, 803 e 804
% Armas: Faca, Revólver, Corda e Veneno

% 1. A pessoa com a Corda estava no quarto 803.
% 2. Alice estava com o Revólver.
% 3. Carol estava num quarto de número par.
% 4. Dave estava com a Faca.
% 5. Alice e Bob não estavam em quartos adjacentes.
% 6. Nenhum dos homens estava com o Veneno.
% 7. Uma das mulheres estava no último quarto.
% 8. Quem cometeu o assassinato estava hospedado no quarto 802.

solution(Rooms, Assassin, Weapon) :-
  Rooms = [room(801, _, _), room(802, _, _), room(803, _, corda), room(804, _, _)],  
  member(room(_, alice, revolver), Rooms), 
  member(room(_, bob, _), Rooms),     
  member(room(N, carol, _), Rooms),   
  even(N),                           
  not(nextto(room(_, alice, _),room(_, bob, _), Rooms)), 
  not(nextto(room(_, bob,_), room(_, alice, _), Rooms)), 
  member(room(_, _, veneno), Rooms),  
  not(member(room(_, dave, veneno), Rooms)),
  not(member(room(_, bob, veneno), Rooms)),            
  (member(room(804, carol, _), Rooms); member(room(804, alice, _), Rooms)), 
  member(room(802, Assassin, Weapon), Rooms).
  

